
int FUN_ffff0000b8006b74(undefined8 param_1,uint param_2)

{
  undefined4 uVar1;
  long lVar2;
  ulong uVar3;
  int iVar4;
  undefined8 uVar5;
  char *pcVar6;
  long lVar7;
  undefined *puVar8;
  uint uVar9;
  undefined4 *puVar10;
  int *piVar11;
  ulong uVar12;
  uint local_7c;
  undefined8 local_78;
  undefined4 local_70;
  short local_6c [2];
  short local_68 [2];
  uint local_64;
  undefined8 local_48;
  
  puVar8 = &DAT_ffff0000b80ad928 + (ulong)param_2 * 0xb8;
  iVar4 = FUN_ffff0000b800f340
                    (param_1,puVar8,(&PTR_s_mediatek_mt6379_gauge_1_ffff0000b80adae0)[param_2]);
  if (1 < iVar4) {
    return -7;
  }
  uVar12 = (ulong)param_2;
  lVar2 = uVar12 * 0xb8;
  piVar11 = (int *)(&DAT_ffff0000b80ad994 + lVar2);
  *piVar11 = iVar4;
  FUN_ffff0000b800f4dc(puVar8,iVar4);
  uVar5 = FUN_ffff0000b800ecd4(*piVar11);
  (&DAT_ffff0000b80ad8e0)[uVar12 * 0x17] = uVar5;
  FUN_ffff0000b800f530(param_1,puVar8);
  lVar7 = (&DAT_ffff0000b80ad948)[uVar12 * 0x17];
  local_64 = 0;
  local_68[0] = 0;
  *(undefined4 *)(lVar7 + 0xc) = 0xdf847;
  local_7c = *(uint *)(&DAT_ffff0000b80ad960 + lVar2);
  local_6c[0] = 0;
  local_78 = *(undefined8 *)(&DAT_ffff0000b80ad964 + lVar2);
  local_70 = *(undefined4 *)(&DAT_ffff0000b80ad96c + lVar2);
  if (param_2 < 2) {
    if ((&DAT_ffff0000b80ad950)[uVar12 * 0x2e] == 0) {
      FUN_ffff0000b800f4dc(puVar8,*piVar11);
      lVar7 = (&DAT_ffff0000b80ad948)[uVar12 * 0x17];
    }
    *(undefined4 *)(lVar7 + 0xc) = 0xdf847;
  }
  else {
    FUN_ffff0000b804c33c
              (s__s__d__bat_idx__u__BUG__ffff0000b809c0f8,
               s_check_and_init_gauge_cust_data_ffff0000b8096e2d,0x80,param_2);
  }
  if ((DAT_ffff0000b810c080 != 0) ||
     (DAT_ffff0000b810c080 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000),
     DAT_ffff0000b810c080 != 0)) {
    if (param_2 < 2) {
      iVar4 = *(int *)(&DAT_ffff0000b80ad8f0 + uVar12 * 0xb8);
      if (iVar4 == 5) {
        uVar9 = 3;
      }
      else if (iVar4 == 10) {
        uVar9 = 2;
      }
      else if (iVar4 == 0x14) {
        uVar9 = 1;
      }
      else {
        uVar9 = 0;
      }
      local_48 = local_48 & 0xffffffff00000000;
      iVar4 = FUN_ffff0000b802127c
                        (DAT_ffff0000b810c080,*(undefined4 *)(&DAT_ffff0000b80ad98c + uVar12 * 0xb8)
                         ,local_6c,2);
      if (iVar4 == 0) {
        iVar4 = FUN_ffff0000b802127c(DAT_ffff0000b810c080,(&local_7c)[uVar9],local_68,2);
        puVar8 = (&PTR_s_mt6379_bat1_ffff0000b80ad8d0)[uVar12];
        if (iVar4 != 0) {
          pcVar6 = s__s__d__Failed_to_read__s_EFUSE_G_ffff0000b808b4ef;
          uVar5 = 0xea;
          goto LAB_ffff0000b8006d90;
        }
        FUN_ffff0000b804c33c
                  (s__s__d___s_dts_r_fg_sel__d__FG_GA_ffff0000b809307f,
                   s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,0xef,puVar8,uVar9,local_6c[0],
                   local_68[0]);
        if (local_6c[0] == local_68[0]) {
          iVar4 = FUN_ffff0000b802120c
                            (DAT_ffff0000b810c080,
                             *(undefined4 *)(&DAT_ffff0000b80ad970 + uVar12 * 0xb8),&local_64);
          if (iVar4 != 0) {
            pcVar6 = s__s__d__Failed_to_read__s_rfg_sel_ffff0000b8092292;
            uVar5 = 0xf5;
            goto LAB_ffff0000b8006d90;
          }
          FUN_ffff0000b804c33c
                    (s__s__d___s_r_fg_sel_value__curren_ffff0000b8095f13,
                     s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,0xfa,puVar8,local_64,uVar9);
          if (local_64 == uVar9) goto LAB_ffff0000b8006eec;
        }
      }
      else {
        puVar8 = (&PTR_s_mt6379_bat1_ffff0000b80ad8d0)[uVar12];
        pcVar6 = s__s__d__Failed_to_read__s_FG_GAIN_ffff0000b809f200;
        uVar5 = 0xe4;
LAB_ffff0000b8006d90:
        FUN_ffff0000b804c33c(pcVar6,s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,uVar5,puVar8);
      }
      iVar4 = FUN_ffff0000b802127c
                        (DAT_ffff0000b810c080,*(undefined4 *)(&DAT_ffff0000b80ad980 + uVar12 * 0xb8)
                         ,&local_48,4);
      if (iVar4 != 0) {
        FUN_ffff0000b804c33c
                  (s__s__d__Failed_to_read__s_CAR_ffff0000b8085ef4,
                   s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,0x105,puVar8);
      }
      iVar4 = FUN_ffff0000b8021244
                        (DAT_ffff0000b810c080,*(undefined4 *)(&DAT_ffff0000b80ad98c + uVar12 * 0xb8)
                         ,local_68,2);
      if (iVar4 == 0) {
        iVar4 = FUN_ffff0000b80211d4
                          (DAT_ffff0000b810c080,
                           *(undefined4 *)(&DAT_ffff0000b80ad970 + uVar12 * 0xb8),uVar9);
        lVar2 = DAT_ffff0000b810c080;
        if (iVar4 == 0) {
          local_48 = local_48 & 0xffffffff;
          uVar1 = *(undefined4 *)(&DAT_ffff0000b80ad974 + uVar12 * 0xb8);
          iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,uVar1,(long)&local_48 + 4);
          if (-1 < iVar4) {
            local_48 = local_48 & 0xfffffffeffffffff;
            iVar4 = FUN_ffff0000b80211d4(lVar2,uVar1);
            lVar2 = DAT_ffff0000b810c080;
            if (iVar4 == 0) {
              local_48 = local_48 & 0xffffffff;
              uVar1 = *(undefined4 *)(&DAT_ffff0000b80ad978 + uVar12 * 0xb8);
              iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,uVar1,(long)&local_48 + 4);
              if (-1 < iVar4) {
                local_48 = local_48 | 0x400000000;
                iVar4 = FUN_ffff0000b80211d4(lVar2,uVar1);
                lVar2 = DAT_ffff0000b810c080;
                if (iVar4 == 0) {
                  uVar1 = *(undefined4 *)(&DAT_ffff0000b80ad978 + uVar12 * 0xb8);
                  local_48 = local_48 & 0xffffffff;
                  iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,uVar1,(long)&local_48 + 4);
                  if (-1 < iVar4) {
                    local_48 = local_48 & 0xfffffffbffffffff;
                    iVar4 = FUN_ffff0000b80211d4(lVar2,uVar1);
                    if (iVar4 == 0) {
                      FUN_ffff0000b804b704(200);
                      lVar2 = DAT_ffff0000b810c080;
                      uVar1 = *(undefined4 *)(&DAT_ffff0000b80ad974 + uVar12 * 0xb8);
                      local_48 = local_48 & 0xffffffff;
                      iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,uVar1,(long)&local_48 + 4);
                      if (-1 < iVar4) {
                        local_48 = local_48 | 0x100000000;
                        iVar4 = FUN_ffff0000b80211d4(lVar2,uVar1);
                        if (iVar4 == 0) {
                          FUN_ffff0000b804b704(33000);
                          goto LAB_ffff0000b8006eec;
                        }
                      }
                      pcVar6 = s__s__d__Failed_to_enable__s_FG_ON_ffff0000b8089898;
                      uVar5 = 0x12d;
                      goto LAB_ffff0000b8006ee4;
                    }
                  }
                  pcVar6 = s__s__d__Failed_to_clr__s_RESET_CA_ffff0000b8095124;
                  uVar5 = 0x124;
                  goto LAB_ffff0000b8006ee4;
                }
              }
              pcVar6 = s__s__d__Failed_to_set__s_RESET_CA_ffff0000b809d2ce;
              uVar5 = 0x11e;
              goto LAB_ffff0000b8006ee4;
            }
          }
          pcVar6 = s__s__d__Failed_to_disable__s_FG_O_ffff0000b8099823;
          uVar5 = 0x118;
        }
        else {
          pcVar6 = s__s__d__Failed_to_set__s_fg_selec_ffff0000b8097c79;
          uVar5 = 0x111;
        }
LAB_ffff0000b8006ee4:
        FUN_ffff0000b804c33c(pcVar6,s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,uVar5,puVar8);
      }
    }
    else {
      FUN_ffff0000b804c33c
                (s__s__d__bat_idx__u__BUG__ffff0000b809c0f8,
                 s_mt6379_config_rfg_gain_error_ffff0000b80a0ccd,0xd8,1);
    }
  }
LAB_ffff0000b8006eec:
  if (((&DAT_ffff0000b80ad8e8)[uVar12 * 0x2e] != 0) &&
     ((local_48 = 0, DAT_ffff0000b810c080 != 0 ||
      (DAT_ffff0000b810c080 = FUN_ffff0000b8020e20(s_mt6379_ffff0000b8094000),
      DAT_ffff0000b810c080 != 0)))) {
    puVar10 = (undefined4 *)(&DAT_ffff0000b80ad958 + uVar12 * 0xb8);
    iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,*puVar10,(long)&local_48 + 4);
    uVar3 = local_48;
    lVar2 = DAT_ffff0000b810c080;
    if (iVar4 == 0) {
      local_48 = local_48 & 0x1ffffffff;
      if ((uVar3 & 0x100000000) != 0) {
        uVar1 = *puVar10;
        local_7c = 0;
        iVar4 = FUN_ffff0000b802120c(DAT_ffff0000b810c080,uVar1,&local_7c);
        if (iVar4 < 0) goto LAB_ffff0000b8006f94;
        local_7c = local_7c & 0xfffffffe;
        iVar4 = FUN_ffff0000b80211d4(lVar2,uVar1);
        if (iVar4 != 0) goto LAB_ffff0000b8006f94;
      }
      FUN_ffff0000b802120c(DAT_ffff0000b810c080,*puVar10,&local_48);
    }
  }
LAB_ffff0000b8006f94:
  FUN_ffff0000b804c33c
            (s__s__d___s_init_done__index__d__ffff0000b8097c41,s_mt6379_gauge_init_ffff0000b808506d,
             0x26a,(&DAT_ffff0000b80ad938)[uVar12 * 0x17],*piVar11);
  return *piVar11;
}


