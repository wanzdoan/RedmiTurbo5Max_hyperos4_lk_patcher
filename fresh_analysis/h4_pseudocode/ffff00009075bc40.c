
undefined8 * FUN_ffff00009075bc40(long param_1,undefined8 param_2)

{
  char *pcVar1;
  undefined *puVar2;
  undefined *puVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  undefined8 *puVar8;
  ulong uVar9;
  undefined8 *puVar10;
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
  
  puVar3 = PTR_PTR_ffff0000908399d8;
  puVar2 = PTR_DAT_ffff0000908399d0;
  uVar5 = *(uint *)PTR_DAT_ffff0000908399d0;
  if (uVar5 == 0) {
LAB_ffff00009075bccc:
    puVar8 = (undefined8 *)0x0;
  }
  else {
    if (uVar5 == 1) {
      puVar10 = *(undefined8 **)PTR_PTR_ffff0000908399d8;
    }
    else {
      if (param_1 != 0) {
        if (0 < (int)uVar5) {
          uVar9 = 0;
          do {
            puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
            iVar4 = FUN_ffff00009077f0ac(*puVar10,param_1);
            if (iVar4 == 0) {
              DAT_ffff000090821948 = (int)uVar9 + 1;
              goto LAB_ffff00009075bfd0;
            }
            uVar9 = uVar9 + 1;
          } while (uVar5 != uVar9);
        }
        goto LAB_ffff00009075bccc;
      }
      local_e0 = 0;
      uStack_d8 = 0;
      local_f0 = (undefined1 *)0x0;
      puStack_e8 = (undefined8 *)0x0;
      FUN_ffff00009077ee50(auStack_840,0,0x750);
      puVar3 = PTR_PTR_ffff0000908399d8;
      if (0 < (int)uVar5) {
        uVar9 = 0;
        do {
          FUN_ffff00009077ee50(&local_f0,0,0x20);
          FUN_ffff00009077ee50(auStack_840,0,0x750);
          puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
          if ((code *)puVar10[2] != (code *)0x0) {
            (*(code *)puVar10[2])(param_2);
          }
          (*(code *)puVar10[3])(auStack_840);
          local_e0 = CONCAT44(local_e0._4_4_,local_838);
          local_f0 = auStack_840;
          puStack_e8 = puVar10;
          FUN_ffff00009077e11c
                    (s_we_will_check_lcm___s__i__d_ffff0000907e1bf5,*puVar10,uVar9 & 0xffffffff);
          lVar6 = FUN_ffff00009075bacc(&local_f0,param_2);
          if (lVar6 == 0) goto LAB_ffff00009075bccc;
          if ((code *)puVar10[9] != (code *)0x0) {
            (*(code *)puVar10[9])(param_2);
          }
          if (((code *)puVar10[0x10] != (code *)0x0) &&
             (iVar4 = (*(code *)puVar10[0x10])(param_2), iVar4 != 0)) {
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
            uVar5 = FUN_ffff00009077f6fc(&local_d0,99,s__soc_dsi_ffff0000907f8a7c);
            if (uVar5 < 99) {
              iVar4 = FUN_ffff00009076da24(param_2,&local_d0);
              pcVar1 = &DAT_ffff0000907d815c;
              if (-1 < iVar4) {
                pcVar1 = s__soc_dsi_ffff0000907f8a7c;
              }
              iVar4 = FUN_ffff00009076da24(param_2,pcVar1);
              if ((iVar4 < 0) ||
                 (lVar7 = FUN_ffff00009076df9c(param_2,iVar4,s_switch_spr_ffff0000907d695a,0),
                 lVar7 == 0)) goto LAB_ffff00009075bf94;
              FUN_ffff00009077eb20((long)&uStack_70 + 4,lVar7,4);
              uVar5 = (uStack_70._4_4_ & 0xff00ff00) >> 8 | (uStack_70._4_4_ & 0xff00ff) << 8;
              uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
              if ((uVar5 < 2) && (local_5d8 != uVar5)) goto LAB_ffff00009075bd20;
            }
            else {
LAB_ffff00009075bf94:
              FUN_ffff00009077e11c(s_set_dsi_panel_index_to_dtb_faile_ffff0000907d99b9);
            }
            FUN_ffff0000907559e4(lVar6,0);
            goto LAB_ffff00009075bfb0;
          }
LAB_ffff00009075bd20:
          FUN_ffff0000907559e4(lVar6,0);
          FUN_ffff0000907551f8(lVar6);
          uVar9 = uVar9 + 1;
        } while ((long)uVar9 < (long)*(int *)puVar2);
      }
      puVar3 = PTR_PTR_ffff0000908399d8;
      if (0 < *(int *)puVar2) {
        uVar9 = 0;
LAB_ffff00009075bec4:
        FUN_ffff00009077ee50(&local_f0,0,0x20);
        FUN_ffff00009077ee50(auStack_840,0,0x750);
        puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
        if ((code *)puVar10[2] != (code *)0x0) {
          (*(code *)puVar10[2])(param_2);
        }
        (*(code *)puVar10[3])(auStack_840);
        local_e0 = CONCAT44(local_e0._4_4_,local_838);
        local_f0 = auStack_840;
        puStack_e8 = puVar10;
        FUN_ffff00009077e11c
                  (s_we_will_check_lcm___s__i__d_ffff0000907e1bf5,*puVar10,uVar9 & 0xffffffff);
        lVar6 = FUN_ffff00009075bacc(&local_f0,param_2);
        if (lVar6 != 0) {
          if ((code *)puVar10[9] != (code *)0x0) {
            (*(code *)puVar10[9])(param_2);
          }
          if (((code *)puVar10[0x10] == (code *)0x0) ||
             (iVar4 = (*(code *)puVar10[0x10])(param_2), iVar4 == 0)) goto LAB_ffff00009075bea0;
          FUN_ffff0000907559e4(lVar6,0);
LAB_ffff00009075bfb0:
          FUN_ffff0000907551f8(lVar6);
          FUN_ffff00009077e11c(s_we_will_use_lcm___s_ffff0000907ece11,*puVar10);
          DAT_ffff000090821948 = (int)uVar9 + 1;
          goto LAB_ffff00009075bfd0;
        }
        goto LAB_ffff00009075bccc;
      }
LAB_ffff00009075bf74:
      FUN_ffff00009077e11c(s_we_have_checked_all_lcm_driver__b_ffff0000907e6882);
      puVar10 = *(undefined8 **)PTR_PTR_ffff0000908399d8;
      DAT_ffff000090821948 = 1;
    }
LAB_ffff00009075bfd0:
    puVar8 = &DAT_ffff00009087b700;
    DAT_ffff00009087b700 = &DAT_ffff00009087b740;
    DAT_ffff00009087b708 = puVar10;
    if ((code *)puVar10[2] != (code *)0x0) {
      (*(code *)puVar10[2])(param_2);
    }
    (*(code *)DAT_ffff00009087b708[3])(DAT_ffff00009087b700);
    DAT_ffff00009087b710 = DAT_ffff00009087b700[2];
    iVar4 = *DAT_ffff00009087b700;
    if (iVar4 == 0) {
      if (DAT_ffff00009087b710 == 0) {
        DAT_ffff00009087b710 = 6;
      }
    }
    else if (iVar4 == 1) {
      if (DAT_ffff00009087b710 == 0) {
        DAT_ffff00009087b710 = 4;
      }
    }
    else if ((iVar4 == 2) && (DAT_ffff00009087b710 == 0)) {
      DAT_ffff00009087b710 = 1;
    }
    DAT_ffff00009087b71c = 0;
    if (DAT_ffff00009087b700[0x1b4] != 0) {
      DAT_ffff00009087b6fc = DAT_ffff00009087b700[0x1b4];
    }
  }
  return puVar8;
LAB_ffff00009075bea0:
  FUN_ffff0000907559e4(lVar6,0);
  FUN_ffff0000907551f8(lVar6);
  uVar9 = uVar9 + 1;
  if ((long)*(int *)puVar2 <= (long)uVar9) goto LAB_ffff00009075bf74;
  goto LAB_ffff00009075bec4;
}


