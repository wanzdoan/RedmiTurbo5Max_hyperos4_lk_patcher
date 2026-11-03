
undefined8 FUN_ffff0000907c5978(undefined8 param_1,undefined4 *param_2)

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
  uVar1 = FUN_ffff0000907c1070(*param_2);
  *in_x5 = 0;
  if ((uVar1 & 0xfffffff8) != 0x1000) {
    FUN_ffff00009077e11c(s__AVB__invalid_pubk_size_ffff0000907f24c2);
    return 0;
  }
  uVar1 = thunk_FUN_ffff00009074b22c(0x1b);
  lVar5 = FUN_ffff000090721d30(s_bypass_check_imgkey_ffff0000907d3e26);
  iVar2 = FUN_ffff00009077eae8(&DAT_ffff000090838658,puVar6,0x200);
  if (iVar2 == 0) {
    FUN_ffff00009077e11c(s__AVB__lk_avb_key___avb_key_cn_ffff0000907df23e);
  }
  iVar3 = FUN_ffff00009077eae8(&DAT_ffff000090838858,puVar6,0x200);
  if (iVar3 == 0) {
    FUN_ffff00009077e11c(s__AVB__lk_avb_key___avb_key_gl_ffff0000907dbdfb);
  }
  iVar4 = FUN_ffff000090721e60();
  if (((iVar4 == 0) || (lVar5 == 0)) ||
     (iVar4 = FUN_ffff00009077f0ac(lVar5,&DAT_ffff0000907df35d), iVar4 != 0)) {
    if ((uVar1 & 0xe) != 0xe) goto LAB_ffff0000907c5a9c;
    if (DAT_ffff00009090d9c4 == '\0') {
      iVar3 = iVar2;
    }
  }
  else {
    FUN_ffff00009077e11c(s__AVB__bypass_check_region_img_ke_ffff0000907e53ea);
LAB_ffff0000907c5a9c:
    if (iVar2 == 0) goto LAB_ffff0000907c5af4;
  }
  if (iVar3 != 0) {
    FUN_ffff00009077e11c(s__AVB__vbmeta_avb_key___ffff0000907f24db);
    uVar7 = 0;
    do {
      FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)((long)puVar6 + uVar7));
      uVar7 = uVar7 + 1;
      if ((uVar7 & 0xf) == 0) {
        FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
      }
    } while (uVar7 != 0x200);
    return 0;
  }
LAB_ffff0000907c5af4:
  *in_x5 = 1;
  return 0;
}


