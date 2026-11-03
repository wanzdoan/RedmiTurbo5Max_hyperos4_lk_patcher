// File offset entry: 0x3d960

undefined8 * FUN_ffff0000b803d960(long param_1,undefined8 param_2)

{
  char *pcVar1;
  undefined *puVar2;
  int iVar3;
  uint uVar4;
  long lVar5;
  long lVar6;
  undefined8 *puVar7;
  ulong uVar8;
  undefined8 *puVar9;
  undefined1 auStack_840 [8];
  undefined4 local_838;
  uint local_5d8;
  undefined1 *local_f0;
  undefined8 *puStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  puVar7 = DAT_ffff0000b8108770;
  puVar2 = PTR_DAT_ffff0000b8108768;
  uVar4 = *(uint *)PTR_DAT_ffff0000b8108768;
  if (uVar4 == 0) {
LAB_ffff0000b803d9ec:
    puVar7 = (undefined8 *)0x0;
  }
  else {
    if (uVar4 == 1) {
      puVar9 = (undefined8 *)*DAT_ffff0000b8108770;
    }
    else {
      if (param_1 != 0) {
        if (0 < (int)uVar4) {
          uVar8 = 0;
          do {
            puVar9 = (undefined8 *)puVar7[uVar8];
            iVar3 = FUN_ffff0000b804d248(*puVar9,param_1);
            if (iVar3 == 0) {
              DAT_ffff0000b80f1198 = (int)uVar8 + 1;
              goto code_r0xffff0000b803dcf4;
            }
            uVar8 = uVar8 + 1;
          } while (uVar4 != uVar8);
        }
        goto LAB_ffff0000b803d9ec;
      }
      local_e0 = 0;
      uStack_d8 = 0;
      local_f0 = (undefined1 *)0x0;
      puStack_e8 = (undefined8 *)0x0;
      FUN_ffff0000b804d070(auStack_840,0,0x750);
      puVar7 = DAT_ffff0000b8108770;
      if (0 < (int)uVar4) {
        uVar8 = 0;
        do {
          FUN_ffff0000b804d070(&local_f0,0,0x20);
          FUN_ffff0000b804d070(auStack_840,0,0x750);
          puVar9 = (undefined8 *)puVar7[uVar8];
          if ((code *)puVar9[2] != (code *)0x0) {
            (*(code *)puVar9[2])(param_2);
          }
          (*(code *)puVar9[3])(auStack_840);
          local_e0 = CONCAT44(local_e0._4_4_,local_838);
          local_f0 = auStack_840;
          puStack_e8 = puVar9;
          FUN_ffff0000b804c33c
                    (s_we_will_check_lcm___s__i__d_ffff0000b808f3d1,*puVar9,uVar8 & 0xffffffff);
          lVar5 = FUN_ffff0000b803d814(&local_f0,param_2);
          if (lVar5 == 0) goto LAB_ffff0000b803d9ec;
          if ((code *)puVar9[9] != (code *)0x0) {
            (*(code *)puVar9[9])(param_2);
          }
          if (((code *)puVar9[0x10] != (code *)0x0) &&
             (iVar3 = (*(code *)puVar9[0x10])(param_2), iVar3 != 0)) {
            local_78 = 0;
            uStack_70 = 0;
            local_88 = 0;
            uStack_80 = 0;
            local_98 = 0;
            uStack_90 = 0;
            local_a8 = 0;
            uStack_a0 = 0;
            local_b8 = 0;
            uStack_b0 = 0;
            local_c8 = 0;
            uStack_c0 = 0;
            local_d0 = 0;
            uVar4 = FUN_ffff0000b804d74c(&local_d0,99,s__soc_dsi_ffff0000b809bd9a);
            if (uVar4 < 99) {
              iVar3 = FUN_ffff0000b8047d28(param_2,&local_d0);
              pcVar1 = &DAT_ffff0000b808a140;
              if (-1 < iVar3) {
                pcVar1 = s__soc_dsi_ffff0000b809bd9a;
              }
              iVar3 = FUN_ffff0000b8047d28(param_2,pcVar1);
              if ((iVar3 < 0) ||
                 (lVar6 = FUN_ffff0000b804803c(param_2,iVar3,s_switch_spr_ffff0000b8089447,0),
                 lVar6 == 0)) goto LAB_ffff0000b803dcb4;
              FUN_ffff0000b804cd40((long)&uStack_70 + 4,lVar6,4);
              uVar4 = (uStack_70._4_4_ & 0xff00ff00) >> 8 | (uStack_70._4_4_ & 0xff00ff) << 8;
              uVar4 = uVar4 >> 0x10 | uVar4 << 0x10;
              if ((uVar4 < 2) && (local_5d8 != uVar4)) goto LAB_ffff0000b803da40;
            }
            else {
LAB_ffff0000b803dcb4:
              FUN_ffff0000b804c33c(s_set_dsi_panel_index_to_dtb_faile_ffff0000b808af2e);
            }
            FUN_ffff0000b8037854(lVar5,0);
            goto LAB_ffff0000b803dcd0;
          }
LAB_ffff0000b803da40:
          FUN_ffff0000b8037854(lVar5,0);
          FUN_ffff0000b8037068(lVar5);
          uVar8 = uVar8 + 1;
        } while ((long)uVar8 < (long)*(int *)puVar2);
      }
      puVar7 = DAT_ffff0000b8108770;
      if (0 < *(int *)puVar2) {
        uVar8 = 0;
LAB_ffff0000b803dbe4:
        FUN_ffff0000b804d070(&local_f0,0,0x20);
        FUN_ffff0000b804d070(auStack_840,0,0x750);
        puVar9 = (undefined8 *)puVar7[uVar8];
        if ((code *)puVar9[2] != (code *)0x0) {
          (*(code *)puVar9[2])(param_2);
        }
        (*(code *)puVar9[3])(auStack_840);
        local_e0 = CONCAT44(local_e0._4_4_,local_838);
        local_f0 = auStack_840;
        puStack_e8 = puVar9;
        FUN_ffff0000b804c33c
                  (s_we_will_check_lcm___s__i__d_ffff0000b808f3d1,*puVar9,uVar8 & 0xffffffff);
        lVar5 = FUN_ffff0000b803d814(&local_f0,param_2);
        if (lVar5 != 0) {
          if ((code *)puVar9[9] != (code *)0x0) {
            (*(code *)puVar9[9])(param_2);
          }
          if (((code *)puVar9[0x10] == (code *)0x0) ||
             (iVar3 = (*(code *)puVar9[0x10])(param_2), iVar3 == 0)) goto LAB_ffff0000b803dbc0;
          FUN_ffff0000b8037854(lVar5,0);
LAB_ffff0000b803dcd0:
          FUN_ffff0000b8037068(lVar5);
          FUN_ffff0000b804c33c(s_we_will_use_lcm___s_ffff0000b8095a46,*puVar9);
          DAT_ffff0000b80f1198 = (int)uVar8 + 1;
          goto code_r0xffff0000b803dcf4;
        }
        goto LAB_ffff0000b803d9ec;
      }
LAB_ffff0000b803dc94:
      FUN_ffff0000b804c33c(s_we_have_checked_all_lcm_driver__b_ffff0000b8091c7e);
      puVar9 = (undefined8 *)*DAT_ffff0000b8108770;
      DAT_ffff0000b80f1198 = 1;
    }
code_r0xffff0000b803dcf4:
    puVar7 = &DAT_ffff0000b8129048;
    DAT_ffff0000b8129048 = &DAT_ffff0000b8129088;
    DAT_ffff0000b8129050 = puVar9;
    if ((code *)puVar9[2] != (code *)0x0) {
      (*(code *)puVar9[2])(param_2);
    }
    (*(code *)DAT_ffff0000b8129050[3])(DAT_ffff0000b8129048);
    DAT_ffff0000b8129058 = DAT_ffff0000b8129048[2];
    iVar3 = *DAT_ffff0000b8129048;
    if (iVar3 == 0) {
      if (DAT_ffff0000b8129058 == 0) {
        DAT_ffff0000b8129058 = 6;
      }
    }
    else if (iVar3 == 1) {
      if (DAT_ffff0000b8129058 == 0) {
        DAT_ffff0000b8129058 = 4;
      }
    }
    else if ((iVar3 == 2) && (DAT_ffff0000b8129058 == 0)) {
      DAT_ffff0000b8129058 = 1;
    }
    DAT_ffff0000b8129064 = 0;
    if (DAT_ffff0000b8129048[0x1b4] != 0) {
      DAT_ffff0000b8129040 = DAT_ffff0000b8129048[0x1b4];
    }
  }
  return puVar7;
LAB_ffff0000b803dbc0:
  FUN_ffff0000b8037854(lVar5,0);
  FUN_ffff0000b8037068(lVar5);
  uVar8 = uVar8 + 1;
  if ((long)*(int *)puVar2 <= (long)uVar8) goto LAB_ffff0000b803dc94;
  goto LAB_ffff0000b803dbe4;
}


