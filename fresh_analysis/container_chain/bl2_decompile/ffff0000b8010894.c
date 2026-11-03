
undefined4 FUN_ffff0000b8010894(void)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  uVar3 = DAT_ffff0000b811aea4;
  iVar1 = FUN_ffff0000b800eb5c(1,0);
  if ((iVar1 < DAT_ffff0000b80ade90) || (DAT_ffff0000b80ade94 <= iVar1)) {
    if ((iVar1 < DAT_ffff0000b80adea0) || (DAT_ffff0000b80adea4 <= iVar1)) {
      if ((iVar1 < DAT_ffff0000b80adeb0) || (DAT_ffff0000b80adeb4 <= iVar1)) {
        if ((iVar1 < DAT_ffff0000b80adec0) || (DAT_ffff0000b80adec4 <= iVar1)) {
          if ((iVar1 < DAT_ffff0000b80aded0) || (DAT_ffff0000b80aded4 <= iVar1)) {
            if ((iVar1 < DAT_ffff0000b80adee0) || (DAT_ffff0000b80adee4 <= iVar1))
            goto LAB_ffff0000b80109b0;
            lVar2 = 5;
          }
          else {
            lVar2 = 4;
          }
        }
        else {
          lVar2 = 3;
        }
      }
      else {
        lVar2 = 2;
      }
    }
    else {
      lVar2 = 1;
    }
  }
  else {
    lVar2 = 0;
  }
  uVar3 = (&DAT_ffff0000b80ade9c)[lVar2 * 4];
LAB_ffff0000b80109b0:
  FUN_ffff0000b804c33c
            (s__XMCHG_LK___s__temperature__d_bo_ffff0000b8095206,
             s_select_jeita_boot_voltage_ffff0000b808f8e1,iVar1,uVar3);
  return uVar3;
}


