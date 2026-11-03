
undefined8
FUN_ffff000090757e70
          (undefined4 param_1,uint param_2,uint param_3,int param_4,int param_5,int param_6,
          int param_7,uint param_8,uint param_9,int param_10,uint param_11,uint param_12,
          uint param_13,undefined4 param_14,uint param_15,byte param_16)

{
  long lVar1;
  uint *puVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  int iVar8;
  int iVar9;
  int iVar10;
  
  FUN_ffff000090757d84();
  iVar4 = FUN_ffff000090757d84(param_1);
  uVar6 = 0;
  iVar9 = 2;
  if (param_4 < 0x1001821) {
    if (param_4 < 0x1001020) {
      if (param_4 == 0x1001000) {
        uVar7 = 0;
        iVar8 = 6;
        iVar10 = iVar9;
        uVar5 = uVar6;
        goto joined_r0xffff000090758010;
      }
      if (param_4 != 0x100101a) goto code_r0xffff00009075821c;
      uVar6 = 0x10000;
      iVar8 = 0xd;
LAB_ffff000090758020:
      uVar5 = 4;
      goto joined_r0xffff000090758028;
    }
    if (param_4 == 0x1001020) {
      uVar7 = 0x10000;
      iVar8 = 0;
      iVar10 = 2;
      uVar5 = 0;
    }
    else {
      if (param_4 == 0x100103a) {
        uVar6 = 0;
        iVar8 = 0xc;
        goto LAB_ffff000090758020;
      }
      if (param_4 != 0x1001801) goto code_r0xffff00009075821c;
      uVar7 = 0;
      iVar8 = 7;
      iVar10 = 3;
      uVar5 = 1;
    }
    goto joined_r0xffff000090758010;
  }
  iVar8 = 3;
  uVar7 = 0x10000;
  switch(param_4) {
  case 0x1002002:
    uVar7 = 0;
    iVar9 = 8;
code_r0xffff000090757fb8:
    iVar10 = 4;
    uVar5 = 2;
    if (param_2 < 4) {
LAB_ffff0000907580c4:
      if ((param_12 | param_11) < 0x1000) {
        if (param_5 == 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cfcc
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907f1a3e,0x20e,&DAT_ffff0000907d2506
                    );
        }
        lVar1 = (ulong)(param_2 * 0x10000 + 0x10000) + (ulong)(uint)(iVar4 << 0x16);
        puVar2 = (uint *)(lVar1 + -0xffffcd7affc0);
        *(undefined4 *)(lVar1 + -0xffffcd7aff00) = 1;
        *puVar2 = *puVar2 & 0xfffffc00 | *puVar2 & 0xff | (param_3 & 3) << 8;
        *puVar2 = *puVar2 | 1;
        *(uint *)(lVar1 + -0xffffcd7afd00) =
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0xfffffe00 |
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0xff | (param_15 & 1) << 8;
        *(uint *)(lVar1 + -0xffffcd7afd00) =
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0xffffff00 | (uint)param_16;
        *(uint *)(lVar1 + -0xffffcd7afd00) =
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0x80000000 |
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0x3fffffff | (param_13 & 1) << 0x1e;
        *(uint *)(lVar1 + -0xffffcd7afd00) =
             *(uint *)(lVar1 + -0xffffcd7afd00) & 0xffffff00 | (uint)param_16;
        *(uint *)(lVar1 + -0xffffcd7affb0) = *(uint *)(lVar1 + -0xffffcd7affb0) & 0xfffeffff | uVar7
        ;
        *(uint *)(lVar1 + -0xffffcd7affb0) = *(uint *)(lVar1 + -0xffffcd7affb0) & 0xfffffff0 | uVar5
        ;
        uVar6 = param_8 & 0xffff | 0x80840000;
        if (iVar9 != 0xc || param_3 == 1) {
          uVar6 = param_8 & 0xffff;
        }
        *(uint *)(lVar1 + -0xffffcd7affbc) = param_9 | param_10 << 0x10;
        *(uint *)(lVar1 + -0xffffcd7af0c0) = param_5 + param_8 * param_7 + iVar10 * param_6;
        *(uint *)(lVar1 + -0xffffcd7affb8) = param_11 | param_12 << 0x10;
        *(undefined4 *)(lVar1 + -0xffffcd7afd14) = param_14;
        *(uint *)(lVar1 + -0xffffcd7afd0c) = uVar6;
        *(undefined4 *)(lVar1 + -0xffffcd7af010) = 2;
        return 0;
      }
    }
LAB_ffff0000907581e8:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907f1a3e,0x20a,
               s__dst_w_<__OVL_MAX_WIDTH)_&&_(dst_ffff0000907f8a36);
  case 0x1002003:
    uVar7 = 0;
    iVar8 = 9;
  case 0x1002023:
    iVar10 = 4;
    uVar5 = 3;
joined_r0xffff000090758010:
    iVar9 = iVar8;
    if (3 < param_2) goto LAB_ffff0000907581e8;
    goto LAB_ffff0000907580c4;
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
code_r0xffff00009075821c:
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907f1a3e,0x2a9,&DAT_ffff0000907d2506);
  case 0x1002022:
    goto code_r0xffff000090757fb8;
  default:
    iVar8 = 10;
    uVar5 = 4;
    iVar9 = 0xb;
    uVar3 = 0;
    switch(param_4) {
    case 0x1101044:
      break;
    case 0x1101046:
    case 0x1101047:
    case 0x1101048:
    case 0x1101049:
    case 0x110104a:
    case 0x110104b:
    case 0x110104c:
    case 0x110104d:
    case 0x110104e:
    case 0x110104f:
    case 0x1101050:
    case 0x1101051:
    case 0x1101052:
    case 0x1101053:
    case 0x1101054:
    case 0x1101055:
    case 0x1101056:
    case 0x1101057:
    case 0x1101058:
    case 0x1101059:
    case 0x110105a:
    case 0x110105b:
    case 0x110105c:
    case 0x110105d:
    case 0x110105e:
    case 0x110105f:
    case 0x1101060:
    case 0x1101061:
    case 0x1101062:
    case 0x1101063:
      goto code_r0xffff00009075821c;
    case 0x1101064:
      uVar6 = 0x10000;
      iVar8 = 4;
      break;
    case 0x1101065:
      iVar9 = 5;
      uVar3 = 0x10000;
    case 0x1101045:
      uVar6 = uVar3;
      iVar8 = iVar9;
      uVar5 = 5;
      break;
    default:
      if (param_4 != 0x1001821) goto code_r0xffff00009075821c;
      uVar5 = 1;
      iVar10 = 3;
      iVar8 = 1;
      goto joined_r0xffff000090758010;
    }
joined_r0xffff000090758028:
    iVar10 = 2;
    uVar7 = uVar6;
    goto joined_r0xffff000090758010;
  }
}


