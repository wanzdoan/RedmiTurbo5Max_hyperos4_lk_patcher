
void FUN_ffff0000b8012348(undefined4 param_1)

{
  char cVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  int iVar6;
  int *piVar7;
  int iVar8;
  long lVar9;
  int iVar10;
  int iVar11;
  uint local_44;
  uint local_28;
  uint local_24;
  
  iVar2 = FUN_ffff0000b800eb5c(1,0);
  local_44 = 0;
  FUN_ffff0000b8012974(&local_44);
  switch(param_1) {
  default:
    iVar8 = DAT_ffff0000b811aebc;
    iVar11 = DAT_ffff0000b811aebc;
    break;
  case 2:
    iVar8 = DAT_ffff0000b811aed4;
    iVar11 = DAT_ffff0000b811aed4;
    break;
  case 3:
    iVar8 = 0x2ee;
    iVar11 = iVar8;
    if (local_44 < 0x53f048) {
      iVar8 = DAT_ffff0000b811aed0;
      iVar11 = DAT_ffff0000b811aed0;
    }
    break;
  case 4:
    piVar7 = &DAT_ffff0000b811aec0;
    goto LAB_ffff0000b8012410;
  case 8:
    piVar7 = &DAT_ffff0000b811aec8;
LAB_ffff0000b8012410:
    iVar8 = piVar7[1];
    iVar11 = *piVar7;
  }
  local_28 = 0;
  uVar3 = FUN_ffff0000b80139e8();
  iVar4 = FUN_ffff0000b80129b4(&local_28);
  if (iVar4 == 0) {
    uVar5 = local_28 / 1000;
  }
  else {
    local_24 = 0;
    uVar5 = FUN_ffff0000b80207a8(0,&local_24,0);
    if (-1 < (int)uVar5) {
      uVar5 = local_24;
    }
  }
  cVar1 = *PTR_DAT_ffff0000b8108710;
  if (999 < (uVar3 & 0xffff) && cVar1 == '\0') {
    uVar5 = uVar3 & 0xffff;
  }
  FUN_ffff0000b804c33c(s__XMCHG_LK__VBAT____d__gauge_iic__ffff0000b80923dd,uVar5,cVar1);
  if ((iVar2 < DAT_ffff0000b80ade90) || (DAT_ffff0000b80ade94 <= iVar2)) {
    if ((iVar2 < DAT_ffff0000b80adea0) || (DAT_ffff0000b80adea4 <= iVar2)) {
      if ((iVar2 < DAT_ffff0000b80adeb0) || (DAT_ffff0000b80adeb4 <= iVar2)) {
        if ((iVar2 < DAT_ffff0000b80adec0) || (DAT_ffff0000b80adec4 <= iVar2)) {
          if ((iVar2 < DAT_ffff0000b80aded0) || (DAT_ffff0000b80aded4 <= iVar2)) {
            iVar4 = 0;
            if ((iVar2 < DAT_ffff0000b80adee0) || (DAT_ffff0000b80adee4 <= iVar2))
            goto LAB_ffff0000b801258c;
            lVar9 = 5;
          }
          else {
            lVar9 = 4;
          }
        }
        else {
          lVar9 = 3;
        }
      }
      else {
        lVar9 = 2;
      }
    }
    else {
      lVar9 = 1;
    }
  }
  else {
    lVar9 = 0;
  }
  iVar4 = (&DAT_ffff0000b80ade98)[lVar9 * 4];
LAB_ffff0000b801258c:
  iVar6 = 0xc80;
  if (cVar1 == '\0') {
    iVar6 = 3000;
  }
  iVar10 = iVar11;
  if (499 < iVar11) {
    iVar10 = 500;
  }
  if ((int)uVar5 <= iVar6) {
    iVar11 = iVar10;
  }
  if (iVar11 <= iVar4) {
    iVar4 = iVar11;
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK___s_TEMP____d__FCC_____ffff0000b808512a,
             s_select_charging_current_limit_ffff0000b8092407,iVar2,iVar4,iVar8);
  FUN_ffff0000b8012934(iVar8);
  FUN_ffff0000b80128f4(iVar4);
  return;
}


