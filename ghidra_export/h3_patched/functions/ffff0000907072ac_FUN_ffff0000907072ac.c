// ffff0000907072ac  size=1468  FUN_ffff0000907072ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907072ac(undefined8 param_1,uint param_2)

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
  
  puVar8 = &DAT_ffff000090816918 + (ulong)param_2 * 0xb8;
  iVar4 = FUN_ffff00009071f1e0
                    (param_1,puVar8,(&PTR_s_mediatek_mt6379_gauge_1_ffff000090816ad0)[param_2]);
  if (1 < iVar4) {
    return -7;
  }
  uVar12 = (ulong)param_2;
  lVar2 = uVar12 * 0xb8;
  piVar11 = (int *)(&DAT_ffff000090816984 + lVar2);
  *piVar11 = iVar4;
  FUN_ffff00009071f37c(puVar8,iVar4);
  uVar5 = FUN_ffff00009071ec98(*piVar11);
  (&DAT_ffff0000908168d0)[uVar12 * 0x17] = uVar5;
  FUN_ffff00009071f3d0(param_1,puVar8);
  lVar7 = (&DAT_ffff000090816938)[uVar12 * 0x17];
  local_64 = 0;
  local_68[0] = 0;
  *(undefined4 *)(lVar7 + 0xc) = 0xdf847;
  local_7c = *(uint *)(&DAT_ffff000090816950 + lVar2);
  local_6c[0] = 0;
  local_78 = *(undefined8 *)(&DAT_ffff000090816954 + lVar2);
  local_70 = *(undefined4 *)(&DAT_ffff00009081695c + lVar2);
  if (param_2 < 2) {
    if ((&DAT_ffff000090816940)[uVar12 * 0x2e] == 0) {
      FUN_ffff00009071f37c(puVar8,*piVar11);
      lVar7 = (&DAT_ffff000090816938)[uVar12 * 0x17];
    }
    *(undefined4 *)(lVar7 + 0xc) = 0xdf847;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__bat_idx__u__BUG__ffff0000907f906b,
               s_check_and_init_gauge_cust_data_ffff0000907ef184,0x80,param_2);
  }
  if ((_DAT_ffff00009083d0f0 != 0) ||
     (_DAT_ffff00009083d0f0 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea40e),
     _DAT_ffff00009083d0f0 != 0)) {
    if (param_2 < 2) {
      iVar4 = *(int *)(&DAT_ffff0000908168e0 + uVar12 * 0xb8);
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
      iVar4 = FUN_ffff00009073732c
                        (_DAT_ffff00009083d0f0,
                         *(undefined4 *)(&DAT_ffff00009081697c + uVar12 * 0xb8),local_6c,2);
      if (iVar4 == 0) {
        iVar4 = FUN_ffff00009073732c(_DAT_ffff00009083d0f0,(&local_7c)[uVar9],local_68,2);
        puVar8 = (&PTR_s_mt6379_bat1_ffff0000908168c0)[uVar12];
        if (iVar4 != 0) {
          pcVar6 = s__s__d__Failed_to_read__s_EFUSE_G_ffff0000907da3fe;
          uVar5 = 0xea;
          goto LAB_ffff0000907074c8;
        }
        FUN_ffff00009077de54
                  (s__s__d___s_dts_r_fg_sel__d__FG_GA_ffff0000907e8a8b,
                   s_mt6379_config_rfg_gain_error_ffff000090801bca,0xef,puVar8,uVar9,local_6c[0],
                   local_68[0]);
        if (local_6c[0] == local_68[0]) {
          iVar4 = FUN_ffff0000907372bc
                            (_DAT_ffff00009083d0f0,
                             *(undefined4 *)(&DAT_ffff000090816960 + uVar12 * 0xb8),&local_64);
          if (iVar4 != 0) {
            pcVar6 = s__s__d__Failed_to_read__s_rfg_sel_ffff0000907e722a;
            uVar5 = 0xf5;
            goto LAB_ffff0000907074c8;
          }
          FUN_ffff00009077de54
                    (s__s__d___s_r_fg_sel_value__curren_ffff0000907ed88f,
                     s_mt6379_config_rfg_gain_error_ffff000090801bca,0xfa,puVar8,local_64,uVar9);
          if (local_64 == uVar9) goto LAB_ffff000090707624;
        }
      }
      else {
        puVar8 = (&PTR_s_mt6379_bat1_ffff0000908168c0)[uVar12];
        pcVar6 = s__s__d__Failed_to_read__s_FG_GAIN_ffff0000907fe7ae;
        uVar5 = 0xe4;
LAB_ffff0000907074c8:
        FUN_ffff00009077de54(pcVar6,s_mt6379_config_rfg_gain_error_ffff000090801bca,uVar5,puVar8);
      }
      iVar4 = FUN_ffff00009073732c
                        (_DAT_ffff00009083d0f0,
                         *(undefined4 *)(&DAT_ffff000090816970 + uVar12 * 0xb8),&local_48,4);
      if (iVar4 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_read__s_CAR_ffff0000907d0924,
                   s_mt6379_config_rfg_gain_error_ffff000090801bca,0x105,puVar8);
      }
      iVar4 = FUN_ffff0000907372f4
                        (_DAT_ffff00009083d0f0,
                         *(undefined4 *)(&DAT_ffff00009081697c + uVar12 * 0xb8),local_68,2);
      if (iVar4 == 0) {
        iVar4 = FUN_ffff000090737284
                          (_DAT_ffff00009083d0f0,
                           *(undefined4 *)(&DAT_ffff000090816960 + uVar12 * 0xb8),uVar9);
        lVar2 = _DAT_ffff00009083d0f0;
        if (iVar4 == 0) {
          local_48 = local_48 & 0xffffffff;
          uVar1 = *(undefined4 *)(&DAT_ffff000090816964 + uVar12 * 0xb8);
          iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,uVar1,(long)&local_48 + 4);
          if (-1 < iVar4) {
            local_48 = local_48 & 0xfffffffeffffffff;
            iVar4 = FUN_ffff000090737284(lVar2,uVar1);
            lVar2 = _DAT_ffff00009083d0f0;
            if (iVar4 == 0) {
              local_48 = local_48 & 0xffffffff;
              uVar1 = *(undefined4 *)(&DAT_ffff000090816968 + uVar12 * 0xb8);
              iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,uVar1,(long)&local_48 + 4);
              if (-1 < iVar4) {
                local_48 = local_48 | 0x400000000;
                iVar4 = FUN_ffff000090737284(lVar2,uVar1);
                lVar2 = _DAT_ffff00009083d0f0;
                if (iVar4 == 0) {
                  uVar1 = *(undefined4 *)(&DAT_ffff000090816968 + uVar12 * 0xb8);
                  local_48 = local_48 & 0xffffffff;
                  iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,uVar1,(long)&local_48 + 4);
                  if (-1 < iVar4) {
                    local_48 = local_48 & 0xfffffffbffffffff;
                    iVar4 = FUN_ffff000090737284(lVar2,uVar1);
                    if (iVar4 == 0) {
                      FUN_ffff00009077cbd8(200);
                      lVar2 = _DAT_ffff00009083d0f0;
                      uVar1 = *(undefined4 *)(&DAT_ffff000090816964 + uVar12 * 0xb8);
                      local_48 = local_48 & 0xffffffff;
                      iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,uVar1,(long)&local_48 + 4);
                      if (-1 < iVar4) {
                        local_48 = local_48 | 0x100000000;
                        iVar4 = FUN_ffff000090737284(lVar2,uVar1);
                        if (iVar4 == 0) {
                          FUN_ffff00009077cbd8(33000);
                          goto LAB_ffff000090707624;
                        }
                      }
                      pcVar6 = s__s__d__Failed_to_enable__s_FG_ON_ffff0000907d7204;
                      uVar5 = 0x12d;
                      goto LAB_ffff00009070761c;
                    }
                  }
                  pcVar6 = s__s__d__Failed_to_clr__s_RESET_CA_ffff0000907ebc5d;
                  uVar5 = 0x124;
                  goto LAB_ffff00009070761c;
                }
              }
              pcVar6 = s__s__d__Failed_to_set__s_RESET_CA_ffff0000907fae0d;
              uVar5 = 0x11e;
              goto LAB_ffff00009070761c;
            }
          }
          pcVar6 = s__s__d__Failed_to_disable__s_FG_O_ffff0000907f3c95;
          uVar5 = 0x118;
        }
        else {
          pcVar6 = s__s__d__Failed_to_set__s_fg_selec_ffff0000907f0ace;
          uVar5 = 0x111;
        }
LAB_ffff00009070761c:
        FUN_ffff00009077de54(pcVar6,s_mt6379_config_rfg_gain_error_ffff000090801bca,uVar5,puVar8);
      }
    }
    else {
      FUN_ffff00009077de54
                (s__s__d__bat_idx__u__BUG__ffff0000907f906b,
                 s_mt6379_config_rfg_gain_error_ffff000090801bca,0xd8,1);
    }
  }
LAB_ffff000090707624:
  if (((&DAT_ffff0000908168d8)[uVar12 * 0x2e] != 0) &&
     ((local_48 = 0, _DAT_ffff00009083d0f0 != 0 ||
      (_DAT_ffff00009083d0f0 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea40e),
      _DAT_ffff00009083d0f0 != 0)))) {
    puVar10 = (undefined4 *)(&DAT_ffff000090816948 + uVar12 * 0xb8);
    iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,*puVar10,(long)&local_48 + 4);
    uVar3 = local_48;
    lVar2 = _DAT_ffff00009083d0f0;
    if (iVar4 == 0) {
      local_48 = local_48 & 0x1ffffffff;
      if ((uVar3 & 0x100000000) != 0) {
        uVar1 = *puVar10;
        local_7c = 0;
        iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,uVar1,&local_7c);
        if (iVar4 < 0) goto LAB_ffff0000907076cc;
        local_7c = local_7c & 0xfffffffe;
        iVar4 = FUN_ffff000090737284(lVar2,uVar1);
        if (iVar4 != 0) goto LAB_ffff0000907076cc;
      }
      FUN_ffff0000907372bc(_DAT_ffff00009083d0f0,*puVar10,&local_48);
    }
  }
LAB_ffff0000907076cc:
  FUN_ffff00009077de54
            (s__s__d___s_init_done__index__d__ffff0000907f0a96,s_mt6379_gauge_init_ffff0000907cf08b,
             0x26a,(&DAT_ffff000090816928)[uVar12 * 0x17],*piVar11);
  return *piVar11;
}


