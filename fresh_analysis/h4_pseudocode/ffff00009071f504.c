
void FUN_ffff00009071f504(long param_1,int param_2)

{
  undefined8 *puVar1;
  
  puVar1 = *(undefined8 **)(param_1 + 0x20);
  *(undefined4 *)((long)puVar1 + 0x14) = 0x4b;
  *puVar1 = 0x3e800000000;
  puVar1[1] = 0x9503000000032;
  *(undefined4 *)(puVar1 + 5) = 0;
  puVar1[6] = (long)param_2 * 0xa8 + -0xffff6f7e7db0;
  puVar1[3] = 1;
  puVar1[4] = 24000;
  *(undefined4 *)(param_1 + 0x28) = 1;
  return;
}


