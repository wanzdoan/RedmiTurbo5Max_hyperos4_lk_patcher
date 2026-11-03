
void FUN_ffff0000b80075ac(ulong param_1,long param_2)

{
  ulong uVar1;
  
  uVar1 = param_1 & 0xffffffffffffffc0;
  do {
    DC_CIVAC(uVar1);
    uVar1 = uVar1 + 0x40;
  } while (uVar1 < param_1 + param_2);
  DataSynchronizationBarrier(3,3,0);
  return;
}


