// ffff00009075b9a8  size=1096  FUN_ffff00009075b9a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff00009075b9a8(long param_1,undefined8 param_2)

{
  char *pcVar1;
  undefined *puVar2;
  undefined *puVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  undefined1 *puVar8;
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
  
  puVar3 = PTR_PTR_ffff0000908399b8;
  puVar2 = PTR_DAT_ffff0000908399b0;
  uVar5 = *(uint *)PTR_DAT_ffff0000908399b0;
  if (uVar5 == 0) {
LAB_ffff00009075ba34:
    puVar8 = (undefined1 *)0x0;
  }
  else {
    if (uVar5 == 1) {
      puVar10 = *(undefined8 **)PTR_PTR_ffff0000908399b8;
    }
    else {
      if (param_1 != 0) {
        if (0 < (int)uVar5) {
          uVar9 = 0;
          do {
            puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
            iVar4 = FUN_ffff00009077ede4(*puVar10,param_1);
            if (iVar4 == 0) {
              DAT_ffff000090821928 = (int)uVar9 + 1;
              goto LAB_ffff00009075bd38;
            }
            uVar9 = uVar9 + 1;
          } while (uVar5 != uVar9);
        }
        goto LAB_ffff00009075ba34;
      }
      local_e0 = 0;
      uStack_d8 = 0;
      local_f0 = (undefined1 *)0x0;
      puStack_e8 = (undefined8 *)0x0;
      FUN_ffff00009077eb88(auStack_840,0,0x750);
      puVar3 = PTR_PTR_ffff0000908399b8;
      if (0 < (int)uVar5) {
        uVar9 = 0;
        do {
          FUN_ffff00009077eb88(&local_f0,0,0x20);
          FUN_ffff00009077eb88(auStack_840,0,0x750);
          puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
          if ((code *)puVar10[2] != (code *)0x0) {
            (*(code *)puVar10[2])(param_2);
          }
          (*(code *)puVar10[3])(auStack_840);
          local_e0 = CONCAT44(local_e0._4_4_,local_838);
          local_f0 = auStack_840;
          puStack_e8 = puVar10;
          FUN_ffff00009077de54
                    (s_we_will_check_lcm___s__i__d_ffff0000907e1b24,*puVar10,uVar9 & 0xffffffff);
          lVar6 = FUN_ffff00009075b834(&local_f0,param_2);
          if (lVar6 == 0) goto LAB_ffff00009075ba34;
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
            uVar5 = FUN_ffff00009077f434(&local_d0,99,s__soc_dsi_ffff0000907f88e0);
            if (uVar5 < 99) {
              iVar4 = FUN_ffff00009076d75c(param_2,&local_d0);
              pcVar1 = &DAT_ffff0000907d80c6;
              if (-1 < iVar4) {
                pcVar1 = s__soc_dsi_ffff0000907f88e0;
              }
              iVar4 = FUN_ffff00009076d75c(param_2,pcVar1);
              if ((iVar4 < 0) ||
                 (lVar7 = FUN_ffff00009076dcd4(param_2,iVar4,s_switch_spr_ffff0000907d68c4,0),
                 lVar7 == 0)) goto LAB_ffff00009075bcfc;
              FUN_ffff00009077e858((long)&uStack_70 + 4,lVar7,4);
              uVar5 = (uStack_70._4_4_ & 0xff00ff00) >> 8 | (uStack_70._4_4_ & 0xff00ff) << 8;
              uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
              if ((uVar5 < 2) && (local_5d8 != uVar5)) goto LAB_ffff00009075ba88;
            }
            else {
LAB_ffff00009075bcfc:
              FUN_ffff00009077de54(s_set_dsi_panel_index_to_dtb_faile_ffff0000907d9922);
            }
            FUN_ffff00009075574c(lVar6,0);
            goto LAB_ffff00009075bd18;
          }
LAB_ffff00009075ba88:
          FUN_ffff00009075574c(lVar6,0);
          FUN_ffff000090754f60(lVar6);
          uVar9 = uVar9 + 1;
        } while ((long)uVar9 < (long)*(int *)puVar2);
      }
      puVar3 = PTR_PTR_ffff0000908399b8;
      if (0 < *(int *)puVar2) {
        uVar9 = 0;
LAB_ffff00009075bc2c:
        FUN_ffff00009077eb88(&local_f0,0,0x20);
        FUN_ffff00009077eb88(auStack_840,0,0x750);
        puVar10 = *(undefined8 **)(puVar3 + uVar9 * 8);
        if ((code *)puVar10[2] != (code *)0x0) {
          (*(code *)puVar10[2])(param_2);
        }
        (*(code *)puVar10[3])(auStack_840);
        local_e0 = CONCAT44(local_e0._4_4_,local_838);
        local_f0 = auStack_840;
        puStack_e8 = puVar10;
        FUN_ffff00009077de54
                  (s_we_will_check_lcm___s__i__d_ffff0000907e1b24,*puVar10,uVar9 & 0xffffffff);
        lVar6 = FUN_ffff00009075b834(&local_f0,param_2);
        if (lVar6 != 0) {
          if ((code *)puVar10[9] != (code *)0x0) {
            (*(code *)puVar10[9])(param_2);
          }
          if (((code *)puVar10[0x10] == (code *)0x0) ||
             (iVar4 = (*(code *)puVar10[0x10])(param_2), iVar4 == 0)) goto LAB_ffff00009075bc08;
          FUN_ffff00009075574c(lVar6,0);
LAB_ffff00009075bd18:
          FUN_ffff000090754f60(lVar6);
          FUN_ffff00009077de54(s_we_will_use_lcm___s_ffff0000907ecc8e,*puVar10);
          DAT_ffff000090821928 = (int)uVar9 + 1;
          goto LAB_ffff00009075bd38;
        }
        goto LAB_ffff00009075ba34;
      }
LAB_ffff00009075bcdc:
      FUN_ffff00009077de54(s_we_have_checked_all_lcm_driver__b_ffff0000907e676e);
      puVar10 = *(undefined8 **)PTR_PTR_ffff0000908399b8;
      DAT_ffff000090821928 = 1;
    }
LAB_ffff00009075bd38:
    puVar8 = &DAT_ffff00009087b668;
    _DAT_ffff00009087b668 = (int *)&DAT_ffff00009087b6a8;
    _DAT_ffff00009087b670 = puVar10;
    if ((code *)puVar10[2] != (code *)0x0) {
      (*(code *)puVar10[2])(param_2);
    }
    (*(code *)_DAT_ffff00009087b670[3])(_DAT_ffff00009087b668);
    _DAT_ffff00009087b678 = _DAT_ffff00009087b668[2];
    iVar4 = *_DAT_ffff00009087b668;
    if (iVar4 == 0) {
      if (_DAT_ffff00009087b678 == 0) {
        _DAT_ffff00009087b678 = 6;
      }
    }
    else if (iVar4 == 1) {
      if (_DAT_ffff00009087b678 == 0) {
        _DAT_ffff00009087b678 = 4;
      }
    }
    else if ((iVar4 == 2) && (_DAT_ffff00009087b678 == 0)) {
      _DAT_ffff00009087b678 = 1;
    }
    _DAT_ffff00009087b684 = 0;
    if (_DAT_ffff00009087b668[0x1b4] != 0) {
      _DAT_ffff00009087b664 = _DAT_ffff00009087b668[0x1b4];
    }
  }
  return puVar8;
LAB_ffff00009075bc08:
  FUN_ffff00009075574c(lVar6,0);
  FUN_ffff000090754f60(lVar6);
  uVar9 = uVar9 + 1;
  if ((long)*(int *)puVar2 <= (long)uVar9) goto LAB_ffff00009075bcdc;
  goto LAB_ffff00009075bc2c;
}


