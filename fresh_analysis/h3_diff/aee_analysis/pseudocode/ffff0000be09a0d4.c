
undefined8 FUN_ffff0000be09a0d4(undefined8 param_1,undefined4 *param_2)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  undefined1 *in_x5;
  undefined4 *puVar6;
  ulong uVar7;
  
  if (param_2 == (undefined4 *)0x0) {
    return 2;
  }
  if (in_x5 == (undefined1 *)0x0) {
    return 2;
  }
  puVar6 = param_2 + 2;
  uVar1 = FUN_ffff0000be0965a4(*param_2);
  *in_x5 = 0;
  if ((uVar1 & 0xfffffff8) != 0x1000) {
    FUN_ffff0000be05fd98(s__AVB__invalid_pubk_size_ffff0000be0bb998);
    return 0;
  }
  uVar1 = thunk_FUN_ffff0000be0364a4(0x1b);
  lVar5 = FUN_ffff0000be01fa8c(s_bypass_check_imgkey_ffff0000be0a6a75);
  iVar2 = FUN_ffff0000be060778(&DAT_ffff0000be0f4780,puVar6,0x200);
  if (iVar2 == 0) {
    FUN_ffff0000be05fd98(s__AVB__lk_avb_key___avb_key_cn_ffff0000be0ae25b);
  }
  iVar3 = FUN_ffff0000be060778(&DAT_ffff0000be0f4980,puVar6,0x200);
  if (iVar3 == 0) {
    FUN_ffff0000be05fd98(s__AVB__lk_avb_key___avb_key_gl_ffff0000be0ac2b6);
  }
  iVar4 = FUN_ffff0000be01fbbc();
  if (((iVar4 == 0) || (lVar5 == 0)) ||
     (iVar4 = FUN_ffff0000be060cb8(lVar5,&DAT_ffff0000be0ae542), iVar4 != 0)) {
    if ((uVar1 & 0xe) != 0xe) goto LAB_ffff0000be09a1f8;
    if (DAT_ffff0000be1b7e34 == '\0') {
      iVar3 = iVar2;
    }
  }
  else {
    FUN_ffff0000be05fd98(s__AVB__bypass_check_region_img_ke_ffff0000be0b2a0e);
LAB_ffff0000be09a1f8:
    if (iVar2 == 0) goto LAB_ffff0000be09a250;
  }
  if (iVar3 != 0) {
    FUN_ffff0000be05fd98(s__AVB__vbmeta_avb_key___ffff0000be0bb9b1);
    uVar7 = 0;
    do {
      FUN_ffff0000be05fd98(s_0x_x_ffff0000be0c390e,*(undefined1 *)((long)puVar6 + uVar7));
      uVar7 = uVar7 + 1;
      if ((uVar7 & 0xf) == 0) {
        FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
      }
    } while (uVar7 != 0x200);
    return 0;
  }
LAB_ffff0000be09a250:
  *in_x5 = 1;
  return 0;
}


