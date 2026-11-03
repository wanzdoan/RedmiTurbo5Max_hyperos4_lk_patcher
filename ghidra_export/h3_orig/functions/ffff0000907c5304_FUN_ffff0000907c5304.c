// ffff0000907c5304  size=416  FUN_ffff0000907c5304

undefined8 FUN_ffff0000907c5304(undefined8 param_1,undefined4 *param_2)

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
  uVar1 = FUN_ffff0000907c09fc(*param_2);
  *in_x5 = 0;
  if ((uVar1 & 0xfffffff8) != 0x1000) {
    FUN_ffff00009077de54(s__AVB__invalid_pubk_size_ffff0000907f2336);
    return 0;
  }
  uVar1 = thunk_FUN_ffff00009074af94(0x1b);
  lVar5 = FUN_ffff000090721af0(s_bypass_check_imgkey_ffff0000907d3de9);
  iVar2 = FUN_ffff00009077e820(&DAT_ffff000090838638,puVar6,0x200);
  if (iVar2 == 0) {
    FUN_ffff00009077de54(s__AVB__lk_avb_key___avb_key_cn_ffff0000907df19b);
  }
  iVar3 = FUN_ffff00009077e820(&DAT_ffff000090838838,puVar6,0x200);
  if (iVar3 == 0) {
    FUN_ffff00009077de54(s__AVB__lk_avb_key___avb_key_gl_ffff0000907dbcf0);
  }
  iVar4 = FUN_ffff000090721c20();
  if (((iVar4 == 0) || (lVar5 == 0)) ||
     (iVar4 = FUN_ffff00009077ede4(lVar5,&DAT_ffff0000907df2ba), iVar4 != 0)) {
    if ((uVar1 & 0xe) != 0xe) goto LAB_ffff0000907c5428;
    if (DAT_ffff00009090d904 == '\0') {
      iVar3 = iVar2;
    }
  }
  else {
    FUN_ffff00009077de54(s__AVB__bypass_check_region_img_ke_ffff0000907e52e6);
LAB_ffff0000907c5428:
    if (iVar2 == 0) goto LAB_ffff0000907c5480;
  }
  if (iVar3 != 0) {
    FUN_ffff00009077de54(s__AVB__vbmeta_avb_key___ffff0000907f234f);
    uVar7 = 0;
    do {
      FUN_ffff00009077de54(s_0x_x_ffff0000907fe4a9,*(undefined1 *)((long)puVar6 + uVar7));
      uVar7 = uVar7 + 1;
      if ((uVar7 & 0xf) == 0) {
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      }
    } while (uVar7 != 0x200);
    return 0;
  }
LAB_ffff0000907c5480:
  *in_x5 = 1;
  return 0;
}


