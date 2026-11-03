
int FUN_ffff0000b80109dc(void)

{
  int *piVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  uint uVar8;
  int iVar9;
  uint uVar10;
  ulong uVar11;
  long lVar12;
  int iVar13;
  uint local_6c;
  uint local_68;
  uint local_64;
  
  iVar3 = FUN_ffff0000b800e1a4();
  uVar4 = FUN_ffff0000b800e190();
  uVar2 = DAT_ffff0000b811af28;
  iVar5 = FUN_ffff0000b800eb5c(1,0);
  iVar6 = FUN_ffff0000b8013ba8();
  uVar11 = FUN_ffff0000b8013978();
  uVar7 = FUN_ffff0000b8013440();
  FUN_ffff0000b804c33c
            (s__s__d___XMCHG_LK___s__is_gl_bat__ffff0000b809e29f,
             s_get_boot_battery_voltage_ffff0000b8088b4c,0x3b6,
             s_get_boot_battery_voltage_ffff0000b8088b4c,uVar7 & 1,uVar2);
  local_6c = 0;
  FUN_ffff0000b8012974(&local_6c);
  uVar7 = local_6c;
  local_68 = 0;
  uVar8 = FUN_ffff0000b80139e8();
  iVar9 = FUN_ffff0000b80129b4(&local_68);
  if (iVar9 == 0) {
    uVar10 = local_68 / 1000;
  }
  else {
    local_64 = 0;
    uVar10 = FUN_ffff0000b80207a8(0,&local_64,0);
    if (-1 < (int)uVar10) {
      uVar10 = local_64;
    }
  }
  if (999 < (uVar8 & 0xffff) && *PTR_DAT_ffff0000b8108710 == '\0') {
    uVar10 = uVar8 & 0xffff;
  }
  iVar9 = DAT_ffff0000b811af1c;
  if (DAT_ffff0000b811af1c != 0xc80) {
    iVar9 = 0xd48;
  }
  if (uVar2 != 4) {
    iVar9 = DAT_ffff0000b811aea4;
  }
  if ((iVar6 < 0x191 && -1 < iVar5) || (uVar2 == 0)) {
    iVar13 = iVar9 + -0x32;
    if ((uVar11 & 0xfe) == 0) {
      iVar13 = iVar9;
    }
  }
  else {
    if (DAT_ffff0000b811af1c == 0xd48) {
      if (0xddd < iVar9) {
        iVar9 = 0xdde;
      }
      iVar9 = iVar9 + 100;
    }
    else if (DAT_ffff0000b811af1c == 0xc80) {
      if (0xd15 < iVar9) {
        iVar9 = 0xd16;
      }
      iVar9 = iVar9 + 0x96;
    }
    FUN_ffff0000b804c33c
              (s__s__d__enter_temperature_or_cycl_ffff0000b808f8fb,
               s_get_boot_battery_voltage_ffff0000b8088b4c,0x3d2);
    iVar13 = iVar9;
  }
  if ((0 < iVar5) && (DAT_ffff0000b811aeac = 0xd48, iVar6 < 0x191)) {
    DAT_ffff0000b811aeac = 0xce4;
  }
  if ((((DAT_ffff0000b811aea3 == '\0') || (iVar3 != 0)) || (0x1e < uVar4)) ||
     ((((1 << (ulong)(uVar4 & 0x1f) & 0x40000022U) == 0 || (DAT_ffff0000b811aeac < (int)uVar10)) ||
      (uVar7 < 0x262988)))) {
    FUN_ffff0000b804c33c
              (s__XMCHG_LK___s__skip_boot_voltage_ffff0000b8093183,
               s_get_boot_battery_voltage_ffff0000b8088b4c);
    iVar5 = 0;
    goto LAB_ffff0000b8010c8c;
  }
  piVar1 = (int *)(&DAT_ffff0000b80a2560 + (ulong)DAT_ffff0000b811af28 * 4);
  if (3 < DAT_ffff0000b811af28 - 1) {
    piVar1 = &DAT_ffff0000b811aea4;
  }
  iVar5 = *piVar1;
  FUN_ffff0000b804c33c
            (s__XMCHG_LK___s__chg_type__d_boot__ffff0000b8090806,
             s_select_chg_type_boot_voltage_ffff0000b808995e,(ulong)DAT_ffff0000b811af28,iVar5);
  if (iVar5 <= iVar13) {
    iVar5 = iVar13;
  }
  iVar6 = FUN_ffff0000b8010894();
  iVar9 = DAT_ffff0000b811aea4;
  if (iVar6 <= iVar5) {
    iVar6 = iVar5;
  }
  iVar5 = FUN_ffff0000b8013ba8();
  if ((iVar5 < DAT_ffff0000b80adef0) || (DAT_ffff0000b80adef4 <= iVar5)) {
    if ((DAT_ffff0000b80adefc <= iVar5) && (iVar5 < DAT_ffff0000b80adf00)) {
      lVar12 = 1;
      goto LAB_ffff0000b8010d84;
    }
    if ((DAT_ffff0000b80adf08 <= iVar5) && (iVar5 < DAT_ffff0000b80adf0c)) {
      lVar12 = 2;
      goto LAB_ffff0000b8010d84;
    }
    if ((DAT_ffff0000b80adf14 <= iVar5) && (iVar5 < DAT_ffff0000b80adf18)) {
      lVar12 = 3;
      goto LAB_ffff0000b8010d84;
    }
  }
  else {
    lVar12 = 0;
LAB_ffff0000b8010d84:
    iVar9 = (&DAT_ffff0000b80adef8)[lVar12 * 3];
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK___s__cycle_count__d_bo_ffff0000b80a0e79,
             s_select_cycle_count_boot_voltage_ffff0000b808c3fd,iVar5,iVar9);
  if (iVar9 <= iVar6) {
    iVar9 = iVar6;
  }
  FUN_ffff0000b804c33c
            (s__XMCHG_LK___s__final_boot_volt___ffff0000b808997b,
             s_get_boot_battery_voltage_ffff0000b8088b4c,iVar9);
  iVar5 = FUN_ffff0000b8004608();
  iVar13 = iVar9;
  if (((-1 < iVar5) && (iVar13 = DAT_ffff0000b811aeac, iVar5 != 3)) &&
     (iVar13 = iVar9 + DAT_ffff0000b811aea8 * iVar5, DAT_ffff0000b811aeac <= iVar13)) {
    iVar13 = DAT_ffff0000b811aeac;
  }
LAB_ffff0000b8010c8c:
  FUN_ffff0000b804c33c
            (s__XMCHG_LK____s__boot___d__d__ena_ffff0000b809d496,
             s_get_boot_battery_voltage_ffff0000b8088b4c,iVar3,uVar4,DAT_ffff0000b811aea3,
             (ulong)uVar7 / 1000,uVar10,iVar13,DAT_ffff0000b811aea4,DAT_ffff0000b811aea8,
             DAT_ffff0000b811aeac,iVar5);
  return iVar13;
}


