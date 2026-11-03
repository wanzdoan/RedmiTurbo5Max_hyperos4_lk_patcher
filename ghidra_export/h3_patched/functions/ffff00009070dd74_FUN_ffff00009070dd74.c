// ffff00009070dd74  size=176  FUN_ffff00009070dd74

undefined8 FUN_ffff00009070dd74(int param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = (long)param_1 * 0x17c;
  uVar1 = *(uint *)(lVar2 + -0xffff6f7c27f8);
  *(uint *)(lVar2 + -0xffff6f7c27f8) = uVar1 + 1;
  FUN_ffff00009077de54(s______current_sparsecrc_0x_x______ffff0000907dd82a);
  if (uVar1 < *(uint *)(&DAT_ffff00009083d804 + lVar2)) {
    if (*(int *)(&DAT_ffff00009083d6a4 + (long)param_1 * 0x17c + (ulong)uVar1 * 4) == param_2) {
      uVar3 = 0;
      if (uVar1 + 1 != *(uint *)(&DAT_ffff00009083d804 + lVar2)) {
        return 0;
      }
      goto LAB_ffff00009070de04;
    }
    FUN_ffff00009077de54(s_CRC_ERR__Witch_should_be_0x_x_ffff0000907d73ae);
  }
  uVar3 = 1;
LAB_ffff00009070de04:
  *(undefined4 *)(lVar2 + -0xffff6f7c27f8) = 0;
  return uVar3;
}


