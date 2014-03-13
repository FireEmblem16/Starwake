#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "cuda_runtime.h"

#define LENGTH 100000
#define WIDTH 100
#define HEIGHT 100

float _max(float a, float b);
float _abs(float f);
__global__ void add_vecs(const int len, const float* a, const float* b, float* c);

int main(int argc, char** argv)
{
	srand(time(NULL));

	float* x = (float*)malloc(sizeof(float) * LENGTH);
	float* y = (float*)malloc(sizeof(float) * LENGTH);
	float* z = (float*)malloc(sizeof(float) * LENGTH);
	float* zp = (float*)malloc(sizeof(float) * LENGTH);

	for(int i = 0;i < LENGTH;i++)
	{
		x[i] = rand() / ((float)rand());
		y[i] = rand() / ((float)rand());
		z[i] = 0.0f;

		zp[i] = x[i] + y[i];
	}

	float* d_x;
	float* d_y;
	float* d_z;
	
	cudaMalloc<float>(&d_x,sizeof(float) * LENGTH);
	cudaMalloc<float>(&d_y,sizeof(float) * LENGTH);
	cudaMalloc<float>(&d_z,sizeof(float) * LENGTH);

	cudaMemcpy(d_x,x,LENGTH,cudaMemcpyHostToDevice);
	cudaMemcpy(d_y,y,LENGTH,cudaMemcpyHostToDevice);
	cudaMemcpy(d_z,z,LENGTH,cudaMemcpyHostToDevice);

	dim3 blocks(LENGTH / 256 + 1);
	dim3 threads(256);
	add_vecs<<<blocks,threads>>>(LENGTH,d_x,d_y,d_z);

	cudaMemcpy(z,d_z,LENGTH,cudaMemcpyDeviceToHost);

	float max_err = 0.0f;

	for(int i = 0;i < LENGTH;i++)
		max_err = _max(max_err,_abs(z[i] - zp[i]));

	printf("Maximum error is: %.6f",max_err);
	
	free(x);
	free(y);
	free(z);
	
	cudaFree(d_x);
	cudaFree(d_y);
	cudaFree(d_z);

	return 0;
}

float _max(float a, float b)
{return a > b ? a : b;}

float _abs(float f)
{return f < 0.0f ? -f : f;}

__global__ void add_vecs(const int len, const float* a, const float* b, float* c)
{
	int index = blockIdx.x * blockDim.x + threadIdx.x;
	
	if(index < len)
		c[index] = a[index] + b[index];

	return;
}