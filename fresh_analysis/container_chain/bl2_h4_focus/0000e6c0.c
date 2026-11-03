// File offset entry: 0xe6c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b800e6c0
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5)

{
  ulong uVar1;
  undefined8 uVar2;
  long lVar3;
  
  uVar1 = daif;
  daif = uVar1 | 0x80;
  FUN_ffff0000b8043cb0();
  FUN_ffff0000b8003a3c();
  lVar3 = FUN_ffff0000b8049ec8(param_1);
  if (lVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b744(s_error_translating_entry_physical_ffff0000b80951dc);
  }
  FUN_ffff0000b800e61c(3);
  FUN_ffff0000b80078dc();
  FUN_ffff0000b800e450(lVar3,param_2,param_3,param_4,param_5);
  DataSynchronizationBarrier(3,3,0);
  uVar1 = UnkSytemRegRead(3,0,0xf,3,5);
  UnkSytemRegWrite(3,0,0xf,3,5,uVar1 | 0x30);
  DataSynchronizationBarrier(3,3,0);
  do {
    uVar2 = UnkSytemRegRead(3,0,0xf,3,7);
  } while ((~(uint)uVar2 & 0x30) != 0);
  _DAT_0c000060 = _DAT_0c000060 & 0xfffffff8;
  DataSynchronizationBarrier(3,3,0);
  return;
}


