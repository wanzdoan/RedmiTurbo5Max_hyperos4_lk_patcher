// ffff0000907104b8  size=32  FUN_ffff0000907104b8

void FUN_ffff0000907104b8(void)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 *unaff_x19;
  undefined4 *unaff_x20;
  int unaff_w21;
  long unaff_x22;
  long lVar3;
  long unaff_x25;
  long unaff_x26;
  
  do {
    lVar3 = unaff_x22;
    unaff_x25 = unaff_x25 + -1;
    if (unaff_x25 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x141,s_idx_<_entry_cnt_ffff0000907ea58a
                );
    }
    unaff_x22 = lVar3 + 0x6c;
  } while (*(int *)(lVar3 + 0x48) != unaff_w21);
  if (unaff_x26 != 0) {
    uVar2 = FUN_ffff0000907104d8();
    iVar1 = FUN_ffff00009077ede4(uVar2,lVar3 + 0x6c);
    if (iVar1 == 0) {
      FUN_ffff00009077de54();
      FUN_ffff0000907104b8();
      return;
    }
  }
  *unaff_x20 = *(undefined4 *)(lVar3 + 0x40);
  *unaff_x19 = *(undefined4 *)(lVar3 + 0x44);
  return;
}


