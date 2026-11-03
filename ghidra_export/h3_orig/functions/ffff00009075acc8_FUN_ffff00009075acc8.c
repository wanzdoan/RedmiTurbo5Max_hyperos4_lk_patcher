// ffff00009075acc8  size=684  FUN_ffff00009075acc8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff00009075acc8
          (undefined8 param_1,uint param_2,undefined4 param_3,int param_4,undefined4 param_5,
          uint param_6,uint param_7,undefined8 param_8,undefined4 param_9)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined4 uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  
  uVar5 = 0;
  uVar7 = 0x100;
  if (param_4 < 0x1002002) {
    if (param_4 == 0x1001020) {
      bVar1 = true;
      goto LAB_ffff00009075ae10;
    }
    if (param_4 != 0x1001801) {
      if (param_4 != 0x1001821) goto code_r0xffff00009075adcc;
      uVar5 = 0x100;
    }
    bVar1 = true;
    uVar3 = 3;
    uVar6 = 0x10;
    goto LAB_ffff00009075ae14;
  }
  switch(param_4) {
  case 0x1002004:
  case 0x1002005:
  case 0x1002006:
  case 0x1002007:
  case 0x1002008:
  case 0x1002009:
  case 0x100200a:
  case 0x100200b:
  case 0x100200c:
  case 0x100200d:
  case 0x100200e:
  case 0x100200f:
  case 0x1002010:
  case 0x1002011:
  case 0x1002012:
  case 0x1002013:
  case 0x1002014:
  case 0x1002015:
  case 0x1002016:
  case 0x1002017:
  case 0x1002018:
  case 0x1002019:
  case 0x100201a:
  case 0x100201b:
  case 0x100201c:
  case 0x100201d:
  case 0x100201e:
  case 0x100201f:
  case 0x1002020:
  case 0x1002021:
code_r0xffff00009075adcc:
    uVar7 = 0;
    uVar5 = 0;
    bVar1 = true;
    goto LAB_ffff00009075ae10;
  case 0x1002022:
    uVar5 = 0x100;
  case 0x1002002:
    bVar1 = true;
    uVar3 = 4;
    uVar6 = 0x20;
    break;
  case 0x1002023:
    uVar5 = 0x100;
  case 0x1002003:
    bVar1 = true;
    uVar3 = 4;
    uVar6 = 0x30;
    break;
  default:
    uVar7 = 0x100;
    switch(param_4) {
    case 0x1101044:
      uVar7 = 0;
    case 0x1101064:
      bVar1 = false;
      uVar5 = 0x40;
      break;
    case 0x1101045:
      uVar7 = 0;
    case 0x1101065:
      bVar1 = false;
      uVar5 = 0x50;
      break;
    default:
      goto code_r0xffff00009075adcc;
    }
LAB_ffff00009075ae10:
    uVar3 = 2;
    uVar6 = uVar5;
    uVar5 = uVar7;
  }
LAB_ffff00009075ae14:
  iVar2 = (int)param_1;
  if (iVar2 != 10) {
    if (iVar2 == 0xf) {
      uVar4 = 1;
      goto LAB_ffff00009075ae38;
    }
    if (iVar2 != 0x55) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (uVar3,s_platform_mediatek_mt6991_disp_dd_ffff0000907fa3aa,0x3c,
                 &DAT_ffff0000907d24c9);
    }
  }
  uVar4 = 0;
LAB_ffff00009075ae38:
  if (bVar1) {
    _DAT_ffff000032170014 = _DAT_ffff000032170014 & 0xff0dffff;
  }
  else {
    _DAT_ffff000032170014 = _DAT_ffff000032170014 & 0xff0fffff | 0x620000;
    _DAT_ffff000032170054 = 0x4a9;
    _DAT_ffff000032170058 = 0;
    _DAT_ffff00003217005c = 0x661;
    _DAT_ffff000032170060 = 0x4a9;
    _DAT_ffff000032170064 = 0xfffffe70;
    _DAT_ffff000032170068 = 0xfffffcc0;
    _DAT_ffff00003217006c = 0x4a9;
    _DAT_ffff000032170070 = 0x811;
    _DAT_ffff000032170074 = 0;
    _DAT_ffff000032170078 = 0xfffffff0;
    _DAT_ffff00003217007c = 0xffffff80;
    _DAT_ffff000032170080 = 0xffffff80;
    _DAT_ffff000032170084 = 0;
    _DAT_ffff000032170088 = 0;
    _DAT_ffff00003217008c = 0;
  }
  uVar7 = 0;
  if (param_2 != 0) {
    uVar7 = uVar6;
  }
  uVar6 = 0;
  if (param_2 != 0) {
    uVar6 = uVar5;
  }
  _DAT_ffff000032170010 =
       _DAT_ffff000032170010 & 0xfffffffc | _DAT_ffff000032170010 & 1 | (param_2 & 1) << 1;
  _DAT_ffff000032170024 = _DAT_ffff000032170024 & 0xfffffe0f | uVar7 | uVar6;
  _DAT_ffff000032170000 = 0;
  _DAT_ffff000032170014 = _DAT_ffff000032170014 & 0xffffe000 | param_6 & 0x1fff;
  _DAT_ffff000032170018 = _DAT_ffff000032170018 & 0xfff00000 | param_7 & 0xfffff;
  _DAT_ffff00003217002c = param_5;
  _DAT_ffff000032170f00 = param_3;
  FUN_ffff00009075ab10(param_1,uVar4,param_6,param_7,uVar3,param_6,param_9);
  return 0;
}


