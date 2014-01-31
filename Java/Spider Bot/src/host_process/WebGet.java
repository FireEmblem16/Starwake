package host_process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Facilitate the downloading of remote resources.
 */
class WebGet extends Observable
{
    /** download buffer size in number of bytes (1 kb) */
    private static final int BUFFER_SIZE = 1024;

    /** has the download operation been cancelled? */
    private volatile boolean cancelled = false;

    /**
    * Get the content of the specified source URL and put it into the given
    * target object.
    *
    * @param sourceUrl
    *     URL of the source (e.g. webpage, remote file) to be downloaded
    * @param sourceDescription
    *     String that describes the source (e.g. "webpage", "remote file")
    * @param target
    *     Target object to be populated (e.g. StringBuilder, File)
    * @return
    *     true if the content of the source URL is downloaded successfully;
    *     false otherwise
    */
    synchronized boolean get(final URL sourceUrl, final String sourceDescription, final Object target)
    {
        /* buffered reader for webpage */
        BufferedReader br = null;

        /* buffered input stream for remote file */
        BufferedInputStream bis = null;

        /* buffered output stream for local file */
        BufferedOutputStream bos = null;

        try
        {
            /* reset cancelled state */
            cancelled = false;

            /* check target type */
            TargetType targetType;

            if(target instanceof StringBuilder)
            {
                targetType = TargetType.STRINGBUILDER;
            }
            else if(target instanceof File)
            {
                targetType = TargetType.LOCAL_FILE;
            }
            else
            {
                throw new UnsupportedOperationException("Unsupported target type " + target.getClass());
            }

            /* open HTTP connection */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT,
                    "Opening HTTP connection to " + sourceDescription));

            HttpURLConnection http = null;

            try
            {
                http = (HttpURLConnection) sourceUrl.openConnection();
                http.connect();
            }
            catch (Exception e)
            {
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.ERROR,
                        "Cannot open HTTP connection to " + sourceDescription));
                return false;
            }

            if (cancelled)
            {
                return false;
            }

            /* get URL content length */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT,
                    "Getting size of " + sourceDescription));

            /* get size of webpage in bytes; -1 if unknown */
            final int size = http.getContentLength();

            setChanged();
            notifyObservers(new Observation(
                    ObservationType.SIZE,
                    size));

            if (cancelled)
            {
                return false;
            }

            /* open input stream */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT,
                    "Opening input stream for " + sourceDescription));

            try
            {
                switch (targetType)
                {
                    case STRINGBUILDER:
                        /* open buffered reader for webpage */

                        final String contentType = http.getContentType().toLowerCase(Locale.ENGLISH);

                        /* look for charset, if specified */
                        String charset = null;
                        final Matcher m = Pattern.compile(".*charset[\\s]*=([^;]++).*").matcher(contentType);

                        if (m.find())
                        {
                            charset = m.group(1).trim();
                        }

                        if ((charset != null) && !charset.isEmpty())
                        {
                            try
                            {
                                br = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
                            }
                            catch (Exception e)
                            {
                                br = null;
                            }
                        }

                        if (br == null)
                        {
                            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        }

                        break;

                    case LOCAL_FILE:
                        /* open input stream for remote file */
                        bis = new BufferedInputStream(http.getInputStream());
                        break;
                }
            }
            catch (Exception e)
            {
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.ERROR,
                        "Cannot open input stream for " + sourceDescription));
                return false;
            }

            if (targetType == TargetType.LOCAL_FILE)
            {
                /* open output stream for local file */
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.PROGRESS_TEXT,
                        "Opening output stream for local file"));
                try
                {
                    final File f = (File) target;

                    /* create parent directories, if necessary */
                    final File parent = f.getParentFile();
                    if ((parent != null) && !parent.exists())
                        parent.mkdirs();

                    bos = new BufferedOutputStream(new FileOutputStream(f));
                }
                catch (Exception e)
                {
                    setChanged();
                    notifyObservers(new Observation(
                            ObservationType.ERROR,
                            "Cannot open output stream for local file"));
                    return false;
                }
            }

            /* download content of source URL iteratively */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT,
                    "Downloading " + sourceDescription));

            /* number of bytes downloaded so far */
            int downloaded = 0;

            try
            {
                switch (targetType)
                {
                    case STRINGBUILDER:

                        final char[] charBuffer = new char[BUFFER_SIZE];
                        final StringBuilder sb = (StringBuilder) target;

                        while (true)
                        {
                            if (cancelled)
                            {
                                return false;
                            }


                            final int charCount = br.read(charBuffer, 0, BUFFER_SIZE);

                            /* check for end-of-stream */
                            if (charCount == -1)
                                break;

                            sb.append(charBuffer, 0, charCount);
                            downloaded += charCount; /* may not be accurate because byte != char */

                            if (size > 0)
                            {
                                int percent = (int) (100.0 * downloaded / size);

                                if (percent < 0)
                                {
                                    percent = 0;
                                }
                                else if (percent > 100)
                                {
                                    percent = 100;
                                }

                                setChanged();
                                notifyObservers(new Observation(
                                        ObservationType.PROGRESS_TEXT_PERCENT,
                                        new Object[] {"Downloading " + sourceDescription + ": " + percent + "%", percent}));
                            }
                        }

                        break;

                    case LOCAL_FILE:

                        final byte[] byteBuffer = new byte[BUFFER_SIZE];

                        while (true)
                        {
                            if (cancelled)
                            {
                                return false;
                            }

                            final int byteCount = bis.read(byteBuffer, 0, BUFFER_SIZE);

                            /* check for end-of-stream */
                            if (byteCount == -1)
                                break;

                            bos.write(byteBuffer, 0, byteCount);
                            downloaded += byteCount;

                            /* update progress bound property */
                            if (size > 0)
                            {
                                int percent = (int) (100.0 * downloaded / size);

                                if (percent < 0)
                                {
                                    percent = 0;
                                }
                                else if (percent > 100)
                                {
                                    percent = 100;
                                }

                                setChanged();
                                notifyObservers(new Observation(
                                        ObservationType.PROGRESS_TEXT_PERCENT,
                                        new Object[] {percent + "%", percent}));
                            }
                        }

                        break;
                }
            }
            catch (Exception e)
            {
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.ERROR,
                        "Cannot download " + sourceDescription));
                return false;
            }

            /* downloading completed */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT_PERCENT,
                    new Object[] {"Downloading " + sourceDescription, 100}));

            if (cancelled)
            {
                return false;
            }

            /* close input stream */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT,
                    "Closing input stream for " + sourceDescription));

            try
            {
                switch (targetType)
                {
                    case STRINGBUILDER:
                        br.close();
                        br = null;
                        break;

                    case LOCAL_FILE:
                        bis.close();
                        bis = null;
                        break;
                }
            }
            catch (Exception e)
            {
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.WARNING,
                        "Cannot close input stream for " + sourceDescription));
            }

            if (targetType == TargetType.LOCAL_FILE)
            {
                /* close output stream for local file */
                setChanged();
                notifyObservers(new Observation(
                        ObservationType.PROGRESS_TEXT,
                        "Closing output stream for local file"));

                try
                {
                    bos.close();
                    bos = null;
                }
                catch (Exception e)
                {
                    setChanged();
                    notifyObservers(new Observation(
                            ObservationType.WARNING,
                            "Cannot close output stream for local file"));
                }
            }

            /* task completed successfully */
            setChanged();
            notifyObservers(new Observation(
                    ObservationType.PROGRESS_TEXT_PERCENT,
                    new Object[] {"Completed", 100}));

            setChanged();
            notifyObservers(new Observation(
                    ObservationType.COMPLETED,
                    null));

            return true;
        }
        finally
        {
            /* close buffered reader for webpage */
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (Exception e)
                {
                    /* ignore */
                }
            }

            /* close buffered input stream for remote file */
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (Exception e)
                {
                    /* ignore */
                }
            }

            /* close buffered output stream for local file */
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (Exception e)
                {
                    /* ignore */
                }
            }
        }
    }


    /**
    * Cancel ongoing operation.
    */
    public void cancel()
    {
        cancelled = true;
    }

    /*****************
    * INNER CLASSES *
    *****************/

    /**
    * Represent an observation.
    */
    static class Observation
    {
        /** observation type */
        public ObservationType observationType;

        /** value of the observation */
        public Object value;

        /**
        * Constructor.
        *
        * @param observationType
        *      Observation type
        * @param value
        *      Value of the observation
        */
        Observation(
                final ObservationType observationType,
                final Object value)
        {
            this.observationType = observationType;
            this.value = value;
        }
    }


    /**
    * Observation types.
    */
    static enum ObservationType
    {
        /** progress update with text only */
        PROGRESS_TEXT,

        /** progress update with text and percentage */
        PROGRESS_TEXT_PERCENT,

        /** operation completed */
        COMPLETED,

        /** obtained size of remote resource */
        SIZE,

        /** fatal error has occurred; operation aborted */
        ERROR,

        /** warning issued; can still proceed with operation */
        WARNING
    };


    /**
    * Target types.
    */
    static enum TargetType
    {
        /** StringBuilder object */
        STRINGBUILDER,

        /** local file */
        LOCAL_FILE
    };
}
