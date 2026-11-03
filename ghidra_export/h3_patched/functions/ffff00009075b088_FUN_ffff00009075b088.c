// ffff00009075b088  size=404  FUN_ffff00009075b088

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075b088(undefined8 param_1,long param_2)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  undefined8 uVar5;
  undefined4 uVar6;
  undefined8 in_x7;
  
  iVar3 = *(int *)(param_2 + 0x28c);
  iVar2 = (int)param_1;
  if (((iVar2 != 10) && (iVar2 != 0xf)) && (iVar2 != 0x55)) {
LAB_ffff00009075b204:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fa3aa,0x3c,&DAT_ffff0000907d24c9);
  }
  if (*(int *)(param_2 + 0xc) == 0) {
    if (*(int *)(param_2 + 4) == 0) {
      return 0;
    }
    if (iVar3 == 0) {
      uVar6 = 0;
      uVar1 = 0x1001821;
    }
    else {
      uVar1 = *(undefined4 *)(param_2 + 0x288);
      uVar6 = *(undefined4 *)(param_2 + 0x290);
    }
    FUN_ffff00009075acc4
              (param_1,iVar3 != 0,iVar3,uVar1,uVar6,*(undefined4 *)(param_2 + 0x294),
               *(undefined4 *)(param_2 + 0x298),in_x7,*(undefined4 *)(param_2 + 0x2e0));
    return 0;
  }
  if (iVar2 != 10) {
    if (iVar2 == 0xf) {
      uVar5 = 1;
      goto LAB_ffff00009075b114;
    }
    if (iVar2 != 0x55) goto LAB_ffff00009075b204;
  }
  uVar5 = 0;
LAB_ffff00009075b114:
  _DAT_ffff000032170010 = _DAT_ffff000032170010 & 0xfffffffd;
  _DAT_ffff000032170024 = _DAT_ffff000032170024 & 0xfffffe0f;
  _DAT_ffff000032170f00 = 0;
  _DAT_ffff00003217002c = 0;
  _DAT_ffff000032170000 = 0;
  _DAT_ffff000032170014 = _DAT_ffff000032170014 & 0xff0de000 | *(uint *)(param_2 + 0x928) & 0x1fff;
  _DAT_ffff000032170018 = _DAT_ffff000032170018 & 0xfff00000 | *(uint *)(param_2 + 0x92c) & 0xfffff;
  FUN_ffff00009075ab10(param_1,uVar5,*(uint *)(param_2 + 0x928),*(uint *)(param_2 + 0x92c),3);
  uVar4 = FUN_ffff000090781944();
  if (((uVar4 & 1) != 0) && (iVar3 = FUN_ffff000090783694(), iVar3 == 0xfc11)) {
    _DAT_ffff0000321700a4 =
         (*(uint *)(param_2 + 0x92c) - (*(uint *)(param_2 + 0x92c) >> 3)) * 0x10000;
    _DAT_ffff0000321700c4 = 0xffff7f00;
    _DAT_ffff000032170018 =
         _DAT_ffff000032170018 & 0xfff00000 | *(uint *)(param_2 + 0x92c) >> 3 & 0xfffff;
  }
  return 0;
}


