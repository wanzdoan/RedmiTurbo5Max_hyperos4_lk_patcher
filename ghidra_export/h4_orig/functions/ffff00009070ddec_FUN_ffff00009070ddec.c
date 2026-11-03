// ffff00009070ddec  size=176  FUN_ffff00009070ddec

undefined8 FUN_ffff00009070ddec(int param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = (long)param_1 * 0x17c;
  uVar1 = *(uint *)(lVar2 + -0xffff6f7c27c0);
  *(uint *)(lVar2 + -0xffff6f7c27c0) = uVar1 + 1;
  FUN_ffff00009077e11c(s______current_sparsecrc_0x_x______ffff0000907dd8e4);
  if (uVar1 < *(uint *)(&DAT_ffff00009083d83c + lVar2)) {
    if (*(int *)(&DAT_ffff00009083d6dc + (long)param_1 * 0x17c + (ulong)uVar1 * 4) == param_2) {
      uVar3 = 0;
      if (uVar1 + 1 != *(uint *)(&DAT_ffff00009083d83c + lVar2)) {
        return 0;
      }
      goto LAB_ffff00009070de7c;
    }
    FUN_ffff00009077e11c(s_CRC_ERR__Witch_should_be_0x_x_ffff0000907d7444);
  }
  uVar3 = 1;
LAB_ffff00009070de7c:
  *(undefined4 *)(lVar2 + -0xffff6f7c27c0) = 0;
  return uVar3;
}


