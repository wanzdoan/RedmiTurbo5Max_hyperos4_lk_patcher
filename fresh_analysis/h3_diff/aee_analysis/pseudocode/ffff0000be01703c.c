
undefined8 FUN_ffff0000be01703c(int param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = (long)param_1 * 0x17c;
  uVar1 = *(uint *)(&DAT_ffff0000be107fb0 + lVar2);
  *(uint *)(&DAT_ffff0000be107fb0 + lVar2) = uVar1 + 1;
  FUN_ffff0000be05fd98(s______current_sparsecrc_0x_x______ffff0000be0ad45b);
  if (uVar1 < (uint)(&DAT_ffff0000be107fac)[(long)param_1 * 0x5f]) {
    if ((&DAT_ffff0000be107e4c)[(ulong)uVar1 + (long)param_1 * 0x5f] == param_2) {
      uVar3 = 0;
      if (uVar1 + 1 != (&DAT_ffff0000be107fac)[(long)param_1 * 0x5f]) {
        return 0;
      }
      goto LAB_ffff0000be0170cc;
    }
    FUN_ffff0000be05fd98(s_CRC_ERR__Witch_should_be_0x_x_ffff0000be0a8e6e);
  }
  uVar3 = 1;
LAB_ffff0000be0170cc:
  *(undefined4 *)(&DAT_ffff0000be107fb0 + lVar2) = 0;
  return uVar3;
}


