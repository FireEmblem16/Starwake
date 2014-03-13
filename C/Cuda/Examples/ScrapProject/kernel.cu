#include <stdlib.h>
#include "cuda_runtime.h"

#define LENGTH 100000

__global__ void copy_vec(int len, const float* a, float* b);

int main(int argc, char** argv)
{
	float* x;
	float* y;

	x = (float*)malloc(LENGTH * sizeof(float));
	y = (float*)malloc(LENGTH * sizeof(float));


	for(int i = 0;i < LENGTH;i++)
	{
		x[i] = rand();
		y[i] = 0.0f;
	}

	float* d_x;
	float* d_y;

	cudaMalloc(&d_x,LENGTH * sizeof(float));
	cudaMalloc(&d_y,LENGTH * sizeof(float));

	cudaMemcpy(d_x,x,LENGTH * sizeof(float),cudaMemcpyHostToDevice);
	cudaMemcpy(d_y,y,LENGTH * sizeof(float),cudaMemcpyHostToDevice);

	dim3 blocks(LENGTH / 256 + 1);
	dim3 threads(256);

	copy_vec<<<blocks,threads>>>(LENGTH,d_x,d_y);
	
	cudaMemcpy(y,d_y,LENGTH * sizeof(float),cudaMemcpyDeviceToHost);

	free(x);
	free(y);
	
	cudaFree(d_x);
	cudaFree(d_y);

	return 0;
}

__global__ void copy_vec(int len, const float* a, float* b)
{
	int index = blockIdx.x * blockDim.x + threadIdx.x;

	if(index < len)
		b[index] = 4.0f * a[index] / 2.415f;

	return;
}