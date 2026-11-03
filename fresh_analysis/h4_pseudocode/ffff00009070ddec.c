
undefined8 FUN_ffff00009070ddec(int param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = (long)param_1 * 0x17c;
  uVar1 = *(uint *)(&DAT_ffff00009083d840 + lVar2);
  *(uint *)(&DAT_ffff00009083d840 + lVar2) = uVar1 + 1;
  FUN_ffff00009077e11c(s______current_sparsecrc_0x_x______ffff0000907dd8e4);
  if (uVar1 < (uint)(&DAT_ffff00009083d83c)[(long)param_1 * 0x5f]) {
    if ((&DAT_ffff00009083d6dc)[(ulong)uVar1 + (long)param_1 * 0x5f] == param_2) {
      uVar3 = 0;
      if (uVar1 + 1 != (&DAT_ffff00009083d83c)[(long)param_1 * 0x5f]) {
        return 0;
      }
      goto LAB_ffff00009070de7c;
    }
    FUN_ffff00009077e11c(s_CRC_ERR__Witch_should_be_0x_x_ffff0000907d7444);
  }
  uVar3 = 1;
LAB_ffff00009070de7c:
  *(undefined4 *)(&DAT_ffff00009083d840 + lVar2) = 0;
  return uVar3;
}


