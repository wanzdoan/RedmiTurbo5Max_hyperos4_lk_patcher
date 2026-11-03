
void FUN_ffff000090707c90(ulong param_1,long param_2)

{
  ulong uVar1;
  
  uVar1 = param_1 & 0xffffffffffffffc0;
  do {
    DC_IVAC(uVar1);
    uVar1 = uVar1 + 0x40;
  } while (uVar1 < param_1 + param_2);
  DataSynchronizationBarrier(3,3,0);
  return;
}


