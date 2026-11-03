
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8010e10(void)

{
  undefined4 uVar1;
  uint uVar2;
  undefined *puVar3;
  undefined4 uVar4;
  byte bVar5;
  int iVar6;
  uint uVar7;
  uint uVar8;
  int iVar9;
  int iVar10;
  undefined8 uVar11;
  int iVar12;
  char local_7c [4];
  uint local_78;
  uint local_74;
  char local_70 [4];
  int local_6c;
  uint local_68;
  uint local_64;
  
  local_7c[0] = '\0';
  if (DAT_ffff0000b811af19 != '\0') {
    iVar6 = 0x15180;
    local_64 = 0;
    local_68 = local_68 & 0xffffff00;
    do {
      FUN_ffff0000b802c208();
      FUN_ffff0000b8012a34();
      FUN_ffff0000b804b704(1000000);
      FUN_ffff0000b804c33c
                (s__s__loop_in_lk2_charger_start__ffff0000b809e2d0,
                 s_power_measure_loop_ffff0000b808a7ee);
      FUN_ffff0000b800ecf0(&local_68,&local_64,0);
      iVar6 = iVar6 + -1;
      local_64 = (int)local_64 / 10;
    } while (iVar6 != 0);
  }
  FUN_ffff0000b8012ab4(local_7c);
  if (local_7c[0] != '\0') {
    FUN_ffff0000b804c33c
              (s__s__d___XMCHG_LK___s__ship_mode__ffff0000b808c41d,
               s_mtk_charger_start_ffff0000b80a00e0,0x636,s_mtk_charger_start_ffff0000b80a00e0);
LAB_ffff0000b8011a44:
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b8014dbc(2,3);
  }
  if ((char)DAT_ffff0000b811aea0 == '\0') {
    local_78 = 0;
    iVar6 = FUN_ffff0000b800eb5c(1,0);
    FUN_ffff0000b804c33c
              (s__s__d___XMCHG_LK__check_low_batt_ffff0000b80923b3,
               s_check_low_battery_ffff0000b809b5be,0x5c8,iVar6);
    if (DAT_ffff0000b811aee8 < iVar6) {
      FUN_ffff0000b804c33c
                (s__s__d___XMCHG_LK__tbat_hot___d_>_ffff0000b8096f29,
                 s_check_low_battery_ffff0000b809b5be,0x5ca,iVar6);
      iVar6 = FUN_ffff0000b80128b0(0);
      if (iVar6 < 0) {
        FUN_ffff0000b804c33c
                  (s__s__d___s__enable_disable_chargi_ffff0000b808c3c8,
                   s_charger_enable_charging_ffff0000b809b5a6,0x1e8,
                   s_charger_enable_charging_ffff0000b809b5a6,iVar6);
      }
      else {
        FUN_ffff0000b804c33c
                  (s__s__d___s__disable_charging_ffff0000b8088b2f,
                   s_charger_enable_charging_ffff0000b809b5a6,0x1ed,
                   s_charger_enable_charging_ffff0000b809b5a6);
      }
    }
    local_68 = 0;
    uVar7 = FUN_ffff0000b80139e8();
    iVar6 = FUN_ffff0000b80129b4(&local_68);
    if (iVar6 == 0) {
      uVar8 = local_68 / 1000;
    }
    else {
      local_64 = 0;
      uVar8 = FUN_ffff0000b80207a8(0,&local_64,0);
      if (-1 < (int)uVar8) {
        uVar8 = local_64;
      }
    }
    puVar3 = PTR_DAT_ffff0000b8108710;
    local_64 = 0;
    if (999 < (uVar7 & 0xffff) && *PTR_DAT_ffff0000b8108710 == '\0') {
      uVar8 = uVar7 & 0xffff;
    }
    FUN_ffff0000b8012974(&local_64);
    uVar7 = local_64;
    local_64 = 0;
    uVar11 = FUN_ffff0000b8020958(s_main_pmic_ffff0000b809c0ee);
    FUN_ffff0000b80205b8(uVar11,0x11,&local_64,0);
    uVar2 = local_64;
    FUN_ffff0000b80129b4(&local_78);
    FUN_ffff0000b804c33c
              (s__s__d___XMCHG_LK__VBAT____d__VBU_ffff0000b8088b7a,
               s_check_low_battery_ffff0000b809b5be,0x5d4,uVar8,(ulong)uVar7 / 1000,uVar2,
               (ulong)local_78 / 1000);
    if (((DAT_ffff0000b811af18 == '\0') || (999 < (int)uVar8)) ||
       ((int)uVar2 <= _DAT_ffff0000b811af14)) {
      local_64 = 0;
      iVar9 = FUN_ffff0000b80127f0(&local_64,1);
      iVar6 = DAT_ffff0000b811af28;
      if ((iVar9 == 0) && (999 < (int)local_64)) {
        local_64 = 0;
        iVar6 = FUN_ffff0000b80127f0(&local_64,1);
        uVar7 = local_64;
        if (iVar6 != 0) {
          uVar7 = 0;
        }
        uVar2 = uVar7;
        if (DAT_ffff0000b811af24 != '\0') {
          uVar2 = uVar8;
          uVar8 = uVar7;
        }
        if ((int)uVar8 < DAT_ffff0000b811aea4) {
          if ((int)uVar2 < DAT_ffff0000b811aea4) {
            uVar7 = 3;
          }
          else {
            uVar7 = (uint)(DAT_ffff0000b811aea4 < (int)uVar2);
          }
        }
        else if (DAT_ffff0000b811aea4 < (int)uVar8) {
          uVar7 = (uint)((int)uVar2 < DAT_ffff0000b811aea4) << 1;
        }
        else {
          uVar7 = 0;
        }
        if ((int)uVar8 < DAT_ffff0000b811aea4 + 200) {
          DAT_ffff0000b811af1a = 1;
        }
                    /* WARNING: Could not recover jumptable at 0xffff0000b801132c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        (*(code *)((ulong)(byte)(&DAT_ffff0000b80a2554)[uVar7] * 4 + -0xffff47feecd0))();
        return;
      }
      uVar2 = DAT_ffff0000b811af28 - 1;
      if ((uVar2 < 8) && ((0x8fU >> (ulong)(uVar2 & 0x1f) & 1) != 0)) {
        FUN_ffff0000b8012934(*(undefined4 *)(&DAT_ffff0000b80a2588 + (long)(int)uVar2 * 4));
      }
      iVar9 = FUN_ffff0000b80109dc();
      if ((int)uVar8 < iVar9 + 200) {
        DAT_ffff0000b811af1a = 1;
      }
      DAT_ffff0000b811aea4 = iVar9;
      FUN_ffff0000b804c33c
                (s__s__d___s___d_vbat__d_th__d__chr_ffff0000b808dbff,
                 s_is_low_battery_ffff0000b8099985,0x42d,s_is_low_battery_ffff0000b8099985,
                 (int)uVar8 < iVar9,uVar8,iVar9,iVar6);
      bVar5 = FUN_ffff0000b8013978();
      FUN_ffff0000b804c33c
                (s__s__d___XMCHG_LK__ssoc____d_ffff0000b809a788,s_is_low_battery_ffff0000b8099985,
                 0x431,bVar5);
      if ((0xd47 < (int)uVar8) && (1 < bVar5)) {
        DAT_ffff0000b811af1a = 0;
      }
      FUN_ffff0000b804c33c
                (s__s__d___XMCHG_LK__ssoc____d__thr_ffff0000b809082f,
                 s_is_low_battery_ffff0000b8099985,0x436,bVar5,0xb22);
      iVar6 = DAT_ffff0000b811af28;
      if ((bVar5 < 2 || (int)uVar8 < 0xb22) && ((int)uVar8 < iVar9)) {
        if (uVar7 < 0x262988) {
          FUN_ffff0000b804c33c
                    (s__s__d___XMCHG_LK__no_charger__po_ffff0000b808b5a0,
                     s_check_low_battery_ffff0000b809b5be,0x5e8);
          FUN_ffff0000b801b93c(6,DAT_ffff0000b811af00);
          FUN_ffff0000b8051ef8();
          FUN_ffff0000b804b704(4000000);
          FUN_ffff0000b801ba64();
          FUN_ffff0000b80520cc(0);
          goto LAB_ffff0000b8011a44;
        }
        local_6c = 0;
        local_70[0] = '\0';
        local_74 = 0;
        FUN_ffff0000b804c33c
                  (s__s__d___XMCHG_LK__pre_charge_loo_ffff0000b8090854,
                   s_check_charging_status_ffff0000b808a801,0x48e,DAT_ffff0000b811af28);
        iVar9 = FUN_ffff0000b80128b0(0);
        if (iVar9 < 0) {
          FUN_ffff0000b804c33c
                    (s__s__d___s__disable_charging_fail_ffff0000b80850d4,
                     s_check_charging_status_ffff0000b808a801,0x492,
                     s_check_charging_status_ffff0000b808a801,iVar9);
        }
        local_68 = 0;
        uVar7 = FUN_ffff0000b80139e8();
        iVar9 = FUN_ffff0000b80129b4(&local_68);
        if (iVar9 == 0) {
          uVar8 = local_68 / 1000;
        }
        else {
          local_64 = 0;
          uVar8 = FUN_ffff0000b80207a8(0,&local_64,0);
          if (-1 < (int)uVar8) {
            uVar8 = local_64;
          }
        }
        if (999 < (uVar7 & 0xffff) && *puVar3 == '\0') {
          uVar8 = uVar7 & 0xffff;
        }
        iVar9 = FUN_ffff0000b800eb5c(1,0);
        FUN_ffff0000b804c33c
                  (s__s__d___XMCHG_LK___s__check_VBAT_ffff0000b80899bc,
                   s_check_charging_status_ffff0000b808a801,0x497,
                   s_check_charging_status_ffff0000b808a801,uVar8,DAT_ffff0000b811aea4);
        FUN_ffff0000b80046b4(DAT_ffff0000b811aee0);
        FUN_ffff0000b80129f4(DAT_ffff0000b811aee4);
        FUN_ffff0000b8020158(0);
        switch(iVar6) {
        case 1:
        case 2:
        case 3:
          iVar12 = 3000;
          if (DAT_ffff0000b811af1c != 0xc80) {
            iVar12 = 0xc1c;
          }
          break;
        case 4:
          DAT_ffff0000b811aea4 = DAT_ffff0000b811aea4 + 0x32;
          if (DAT_ffff0000b811aeac <= DAT_ffff0000b811aea4) {
            DAT_ffff0000b811aea4 = DAT_ffff0000b811aeac;
          }
          iVar12 = 0xb54;
          if (DAT_ffff0000b811af1c != 0xc80) {
            iVar12 = 3000;
          }
          break;
        default:
          iVar12 = 0xc1c;
        }
        if (iVar9 < 0) {
          DAT_ffff0000b811aea4 = 0xdde;
          if (DAT_ffff0000b811af1c != 0xc80) {
            DAT_ffff0000b811aea4 = 0xe42;
          }
          iVar12 = 3000;
          if (DAT_ffff0000b811af1c != 0xc80) {
            iVar12 = 0xc1c;
          }
        }
        FUN_ffff0000b80129b4(&local_74);
        FUN_ffff0000b804c33c
                  (s__s__d___XMCHG_LK__T__d__V__d__d__ffff0000b8096017,
                   s_check_charging_status_ffff0000b808a801,0x4d3,iVar9,uVar8,(ulong)local_74 / 1000
                   ,DAT_ffff0000b811aea4,iVar12,DAT_ffff0000b811af1c,DAT_ffff0000b811aee0);
        if ((int)uVar8 < DAT_ffff0000b811aea4) {
          do {
            FUN_ffff0000b802c208();
            FUN_ffff0000b8012a34();
            FUN_ffff0000b8012a74();
            FUN_ffff0000b8012870();
            FUN_ffff0000b80121fc();
            iVar9 = FUN_ffff0000b800eb5c(1,0);
            FUN_ffff0000b804c33c
                      (s__s__d___XMCHG_LK___s__T__d_ffff0000b809f420,
                       s_check_charging_status_ffff0000b808a801,0x4e0,
                       s_check_charging_status_ffff0000b808a801,iVar9);
            if ((DAT_ffff0000b811aee8 < iVar9) || (iVar9 < DAT_ffff0000b811aef0)) {
              iVar10 = FUN_ffff0000b80128b0(0);
              if (iVar10 < 0) {
                FUN_ffff0000b804c33c
                          (s__s__d___s__disable_charging_fail_ffff0000b80850d4,
                           s_check_charging_status_ffff0000b808a801,0x4e4,
                           s_check_charging_status_ffff0000b808a801,iVar10);
              }
              uVar4 = DAT_ffff0000b811aebc;
              uVar1 = DAT_ffff0000b811aec4;
              if (iVar6 != 4) {
                uVar1 = DAT_ffff0000b811aebc;
              }
              FUN_ffff0000b8012934(uVar1);
              FUN_ffff0000b80128f4(uVar4);
              iVar10 = FUN_ffff0000b800eb5c(1,0);
              if ((DAT_ffff0000b811aef4 < iVar10) || (iVar10 < DAT_ffff0000b811aef8)) {
                do {
                  do {
                    FUN_ffff0000b802c208();
                    FUN_ffff0000b8012a34();
                    FUN_ffff0000b804b704(2000000);
                    iVar10 = FUN_ffff0000b800eb5c(1,0);
                  } while (DAT_ffff0000b811aef4 < iVar10);
                } while (iVar10 < DAT_ffff0000b811aef8);
              }
            }
            FUN_ffff0000b8012348(iVar6);
            if (iVar9 < 0) {
              DAT_ffff0000b811aea4 = 0xdde;
              if (DAT_ffff0000b811af1c != 0xc80) {
                DAT_ffff0000b811aea4 = 0xe42;
              }
              iVar12 = 3000;
              if (DAT_ffff0000b811af1c != 0xc80) {
                iVar12 = 0xc1c;
              }
            }
            iVar10 = FUN_ffff0000b80128b0(1);
            if (iVar10 < 0) {
              FUN_ffff0000b804c33c
                        (s__s__d___s__enable_charging_faile_ffff0000b8088bbc,
                         s_check_charging_status_ffff0000b808a801,0x509,
                         s_check_charging_status_ffff0000b808a801,iVar10);
            }
            if ((DAT_ffff0000b811aefc == '\0') || ((int)uVar8 <= iVar12)) {
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
            else if (DAT_ffff0000b811af2c == '\x01') {
              FUN_ffff0000b802c208();
              FUN_ffff0000b80121fc();
              FUN_ffff0000b804b704(1000000);
              iVar10 = FUN_ffff0000b8020094(0);
              if (iVar10 != 0) {
                FUN_ffff0000b8020158(0);
                FUN_ffff0000b8012604();
              }
              FUN_ffff0000b802c208();
              FUN_ffff0000b80121fc();
              FUN_ffff0000b804b704(1000000);
              iVar10 = FUN_ffff0000b8020094(0);
              if (iVar10 != 0) {
                FUN_ffff0000b8020158(0);
                FUN_ffff0000b8012604();
              }
              FUN_ffff0000b802c208();
              FUN_ffff0000b80121fc();
              FUN_ffff0000b804b704(1000000);
              iVar10 = FUN_ffff0000b8020094(0);
              if (iVar10 != 0) {
                FUN_ffff0000b8020158(0);
                FUN_ffff0000b8012604();
              }
              FUN_ffff0000b802c208();
              FUN_ffff0000b80121fc();
              FUN_ffff0000b804b704(1000000);
              iVar10 = FUN_ffff0000b8020094(0);
              if (iVar10 != 0) {
                FUN_ffff0000b8020158(0);
                FUN_ffff0000b8012604();
              }
              FUN_ffff0000b802c208();
              FUN_ffff0000b80121fc();
              FUN_ffff0000b804b704(1000000);
              iVar10 = FUN_ffff0000b8020094(0);
              if (iVar10 != 0) {
                FUN_ffff0000b8020158(0);
                FUN_ffff0000b8012604();
              }
            }
            else {
              FUN_ffff0000b8012604();
              DAT_ffff0000b811af2c = '\x01';
            }
            local_68 = 0;
            uVar7 = FUN_ffff0000b80139e8();
            iVar10 = FUN_ffff0000b80129b4(&local_68);
            if (iVar10 == 0) {
              uVar8 = local_68 / 1000;
            }
            else {
              local_64 = 0;
              uVar8 = FUN_ffff0000b80207a8(0,&local_64,0);
              if (-1 < (int)uVar8) {
                uVar8 = local_64;
              }
            }
            if (999 < (uVar7 & 0xffff) && *puVar3 == '\0') {
              uVar8 = uVar7 & 0xffff;
            }
            if ((int)uVar8 < 1000) break;
            FUN_ffff0000b800ecf0(local_70,&local_6c,0);
            FUN_ffff0000b800ecf0(local_70,&local_6c,1);
            local_6c = local_6c / 10;
            iVar10 = -local_6c;
            if (local_70[0] != '\0') {
              iVar10 = local_6c;
            }
            FUN_ffff0000b804c33c(s_IBAT____d_ffff0000b8096f60,iVar10);
            local_68 = 0;
            uVar7 = FUN_ffff0000b80139e8();
            iVar10 = FUN_ffff0000b80129b4(&local_68);
            if (iVar10 == 0) {
              uVar8 = local_68 / 1000;
            }
            else {
              local_64 = 0;
              uVar8 = FUN_ffff0000b80207a8(0,&local_64,0);
              if (-1 < (int)uVar8) {
                uVar8 = local_64;
              }
            }
            local_64 = 0;
            if (999 < (uVar7 & 0xffff) && *puVar3 == '\0') {
              uVar8 = uVar7 & 0xffff;
            }
            uVar7 = FUN_ffff0000b80207a8(0,&local_64,0);
            if (-1 < (int)uVar7) {
              uVar7 = local_64;
            }
            FUN_ffff0000b804c33c
                      (s__XMCHG_LK__T__d__V__d__d__BootTh_ffff0000b8085102,iVar9,uVar8,uVar7,
                       DAT_ffff0000b811aea4);
          } while ((int)uVar8 < DAT_ffff0000b811aea4);
        }
        FUN_ffff0000b802c208();
      }
    }
  }
  FUN_ffff0000b804c33c
            (s__s__d___s__end_ffff0000b809f410,s_mtk_charger_start_ffff0000b80a00e0,0x646,
             s_mtk_charger_start_ffff0000b80a00e0);
  return;
}


