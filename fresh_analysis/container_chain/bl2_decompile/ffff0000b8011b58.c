
void FUN_ffff0000b8011b58(void)

{
  undefined4 uVar1;
  undefined *puVar2;
  int iVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  int iVar7;
  uint uVar8;
  uint uVar9;
  undefined4 uVar10;
  undefined1 local_70 [4];
  int local_6c;
  uint local_68;
  uint local_64;
  
  iVar3 = DAT_ffff0000b811af28;
  local_6c = 0;
  local_70[0] = 0;
  FUN_ffff0000b804c33c
            (s__s__d___s__charger_type___d_ffff0000b809e2f0,
             s_check_charging_dual_status_ffff0000b8088c25,0x555,
             s_check_charging_dual_status_ffff0000b8088c25,DAT_ffff0000b811af28);
  iVar4 = FUN_ffff0000b80128b0(0);
  if (iVar4 < 0) {
    FUN_ffff0000b804c33c
              (s__s__d___s__disable_charging_fail_ffff0000b80850d4,
               s_check_charging_dual_status_ffff0000b8088c25,0x559,
               s_check_charging_dual_status_ffff0000b8088c25,iVar4);
  }
  local_68 = 0;
  uVar5 = FUN_ffff0000b80139e8();
  iVar4 = FUN_ffff0000b80129b4(&local_68);
  if (iVar4 == 0) {
    uVar6 = local_68 / 1000;
  }
  else {
    local_64 = 0;
    uVar6 = FUN_ffff0000b80207a8(0,&local_64,0);
    if (-1 < (int)uVar6) {
      uVar6 = local_64;
    }
  }
  puVar2 = PTR_DAT_ffff0000b8108710;
  local_64 = 0;
  if (999 < (uVar5 & 0xffff) && *PTR_DAT_ffff0000b8108710 == '\0') {
    uVar6 = uVar5 & 0xffff;
  }
  iVar4 = FUN_ffff0000b80127f0(&local_64,1);
  uVar5 = local_64;
  if (iVar4 != 0) {
    uVar5 = 0;
  }
  FUN_ffff0000b804c33c
            (s__s__d___s__check_VBAT__d_mV___VB_ffff0000b80931ec,
             s_check_charging_dual_status_ffff0000b8088c25,0x55e,
             s_check_charging_dual_status_ffff0000b8088c25,uVar6,uVar5,DAT_ffff0000b811aea4);
  FUN_ffff0000b8020158(0);
  if ((int)uVar6 < DAT_ffff0000b811aea4) {
    while( true ) {
      FUN_ffff0000b802c208();
      FUN_ffff0000b8012a34();
      FUN_ffff0000b8012a74();
      FUN_ffff0000b8012870();
      FUN_ffff0000b80121fc();
      iVar4 = FUN_ffff0000b800eb5c(1,0);
      local_64 = FUN_ffff0000b800eb5c(1,1);
      uVar5 = local_64;
      if ((local_64 == 0xfffffeed) &&
         (iVar7 = FUN_ffff0000b80127f0(&local_64,3), uVar5 = local_64, iVar7 != 0)) {
        uVar5 = 0;
      }
      FUN_ffff0000b804c33c
                (s__s__d___s__T__d___d_ffff0000b808ce9d,
                 s_check_charging_dual_status_ffff0000b8088c25,0x56e,
                 s_check_charging_dual_status_ffff0000b8088c25,iVar4,uVar5);
      uVar10 = DAT_ffff0000b811aebc;
      if (DAT_ffff0000b811aee8 < (int)uVar5) break;
      if ((((int)uVar6 < DAT_ffff0000b811aeb8) || (DAT_ffff0000b811aeec < iVar4)) ||
         (iVar4 < DAT_ffff0000b811aef0)) {
        uVar1 = DAT_ffff0000b811aec4;
        if (iVar3 != 4) {
          uVar1 = DAT_ffff0000b811aebc;
        }
        FUN_ffff0000b8012934(uVar1);
        FUN_ffff0000b80128f4(uVar10);
      }
      else {
        FUN_ffff0000b8012348(iVar3);
      }
      iVar4 = FUN_ffff0000b80128b0(1);
      if (iVar4 < 0) {
        FUN_ffff0000b804c33c
                  (s__s__d___s__enable_charging_faile_ffff0000b8088bbc,
                   s_check_charging_dual_status_ffff0000b8088c25,0x57f,
                   s_check_charging_dual_status_ffff0000b8088c25,iVar4);
      }
      local_64 = 0;
      iVar4 = FUN_ffff0000b80127f0(&local_64,1);
      if ((iVar4 == 0) && (999 < (int)local_64)) {
        local_64 = 0;
        iVar4 = FUN_ffff0000b80127f0(&local_64,1);
        uVar5 = local_64;
        if (iVar4 != 0) {
          uVar5 = 0;
        }
        uVar10 = 0x32;
        if (DAT_ffff0000b811aeb8 + -1000 <= (int)uVar5) {
          uVar10 = 500;
        }
        FUN_ffff0000b8012830(uVar10,1);
        local_64 = 0;
        iVar4 = FUN_ffff0000b80127f0(&local_64,1);
        local_68 = 0;
        uVar5 = local_64;
        if (iVar4 != 0) {
          uVar5 = 0;
        }
        uVar8 = FUN_ffff0000b80139e8();
        iVar4 = FUN_ffff0000b80129b4(&local_68);
        if (iVar4 == 0) {
          uVar9 = local_68 / 1000;
        }
        else {
          local_64 = 0;
          uVar9 = FUN_ffff0000b80207a8(0,&local_64,0);
          if (-1 < (int)uVar9) {
            uVar9 = local_64;
          }
        }
        if (999 < (uVar8 & 0xffff) && *puVar2 == '\0') {
          uVar9 = uVar8 & 0xffff;
        }
        if ((int)(uVar5 - uVar9) < 100) {
          FUN_ffff0000b80127ac(1);
        }
      }
      if ((DAT_ffff0000b811aefc == '\0') || ((int)uVar6 <= DAT_ffff0000b811aeb8)) {
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
      }
      else {
        if ((DAT_ffff0000b811af2d & 1) == 0) {
          FUN_ffff0000b8012604();
          DAT_ffff0000b811af2d = 1;
        }
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        iVar4 = FUN_ffff0000b8020094(0);
        if (iVar4 != 0) {
          FUN_ffff0000b8020158(0);
          FUN_ffff0000b8012604();
        }
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        iVar4 = FUN_ffff0000b8020094(0);
        if (iVar4 != 0) {
          FUN_ffff0000b8020158(0);
          FUN_ffff0000b8012604();
        }
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        iVar4 = FUN_ffff0000b8020094(0);
        if (iVar4 != 0) {
          FUN_ffff0000b8020158(0);
          FUN_ffff0000b8012604();
        }
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        iVar4 = FUN_ffff0000b8020094(0);
        if (iVar4 != 0) {
          FUN_ffff0000b8020158(0);
          FUN_ffff0000b8012604();
        }
        FUN_ffff0000b802c208();
        FUN_ffff0000b80121fc();
        FUN_ffff0000b804b704(1000000);
        iVar4 = FUN_ffff0000b8020094(0);
        if (iVar4 != 0) {
          FUN_ffff0000b8020158(0);
          FUN_ffff0000b8012604();
        }
      }
      local_68 = 0;
      uVar5 = FUN_ffff0000b80139e8();
      iVar4 = FUN_ffff0000b80129b4(&local_68);
      if (iVar4 == 0) {
        uVar8 = local_68 / 1000;
      }
      else {
        local_64 = 0;
        uVar8 = FUN_ffff0000b80207a8(0,&local_64,0);
        if (-1 < (int)uVar8) {
          uVar8 = local_64;
        }
      }
      if (999 < (uVar5 & 0xffff) && *puVar2 == '\0') {
        uVar8 = uVar5 & 0xffff;
      }
      if ((int)uVar8 < 1000) goto LAB_ffff0000b80121d4;
      local_64 = 0;
      iVar4 = FUN_ffff0000b80127f0(&local_64,1);
      if ((iVar4 != 0) || ((int)local_64 < 1000)) goto LAB_ffff0000b80121d4;
      FUN_ffff0000b800ecf0(local_70,&local_6c,0);
      local_64 = 0;
      local_68 = local_68 & 0xffffff00;
      local_6c = local_6c / 10;
      iVar4 = FUN_ffff0000b800ecf0(&local_68,&local_64,1);
      if (iVar4 < 0) {
        FUN_ffff0000b80127f0(&local_64,2);
      }
      if (((iVar3 == 4) && (DAT_ffff0000b811aeb8 < (int)uVar6)) &&
         (iVar4 = FUN_ffff0000b80128b0(0), iVar4 < 0)) {
        FUN_ffff0000b804c33c
                  (s__s__d___s__disable_charging_fail_ffff0000b80850d4,
                   s_check_charging_dual_status_ffff0000b8088c25,0x5b0,
                   s_check_charging_dual_status_ffff0000b8088c25,iVar4);
      }
      local_68 = 0;
      uVar5 = FUN_ffff0000b80139e8();
      iVar4 = FUN_ffff0000b80129b4(&local_68);
      if (iVar4 == 0) {
        uVar6 = local_68 / 1000;
      }
      else {
        local_64 = 0;
        uVar6 = FUN_ffff0000b80207a8(0,&local_64,0);
        if (-1 < (int)uVar6) {
          uVar6 = local_64;
        }
      }
      if (999 < (uVar5 & 0xffff) && *puVar2 == '\0') {
        uVar6 = uVar5 & 0xffff;
      }
      if (DAT_ffff0000b811aea4 <= (int)uVar6) goto LAB_ffff0000b80121d4;
    }
    FUN_ffff0000b804c33c
              (s__s__d___s__Sub_Battery_over_Temp_ffff0000b8099994,
               s_check_charging_dual_status_ffff0000b8088c25,0x572,
               s_check_charging_dual_status_ffff0000b8088c25,uVar5);
  }
LAB_ffff0000b80121d4:
  FUN_ffff0000b802c208();
  return;
}


