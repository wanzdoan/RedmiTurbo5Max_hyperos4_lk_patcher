// ffff00009070ce00  size=1016  FUN_ffff00009070ce00

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070ce00(undefined8 *param_1,int *param_2,undefined4 param_3)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long lVar4;
  ulong uVar5;
  char *pcVar6;
  undefined1 uStack_99;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  int local_34;
  
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_98 = 0;
  local_34 = 1;
  iVar1 = thunk_FUN_ffff0000907b65c4(&local_34);
  if (iVar1 != 0) {
    pcVar6 = s_security_deny_ffff0000907e2521;
    goto LAB_ffff00009070d1b0;
  }
  if ((local_34 == 1) &&
     (iVar1 = FUN_ffff00009077f0ac(s_boot_para_ffff0000907f7bad,param_1), iVar1 != 0)) {
    pcVar6 = s_Flashing_is_not_allowed_in_Lock_s_ffff0000907dbfc5;
    goto LAB_ffff00009070d1b0;
  }
  if (param_1 == (undefined8 *)0x0) {
LAB_ffff00009070cff8:
    lVar3 = FUN_ffff00009077f0d4(&local_98,param_1);
    if (lVar3 == 0) {
      FUN_ffff00009077e11c(s_strcpy_partition_name_from_arg_f_ffff0000907fcbb5);
      pcVar6 = s_unknow_reason_ffff0000907d3e7d;
    }
    else {
      lVar3 = FUN_ffff00009077f544(&local_98,&DAT_ffff0000907f0d88);
      if (lVar3 != 0) {
        lVar4 = FUN_ffff00009077f250(&local_98);
        (&uStack_99)[lVar4] = 0;
        param_1 = &local_98;
        lVar4 = FUN_ffff00009077f250(&local_98);
        (&uStack_99)[lVar4] = 0x61;
      }
      uVar5 = FUN_ffff00009070e1a8(param_1);
      if ((uVar5 & 1) != 0) {
        if (((DAT_ffff000090816d70 == '\0') || (_DAT_ffff00009083dfc8 == 0)) ||
           ((iVar1 = FUN_ffff00009077f394(s_system_ffff0000907edad2,0xffff00009083d3d8,0x20),
            iVar1 != 0 &&
            (iVar1 = FUN_ffff00009077f394(s_super_ffff0000907e5b8c,0xffff00009083d3d8,0x20),
            iVar1 != 0)))) {
          if (*param_2 == -0x12d900c6) {
            FUN_ffff00009070e7ac();
          }
          else {
            FUN_ffff00009070e424(param_1,param_2,param_3);
          }
        }
        else {
          FUN_ffff000090709bec(0xffff00009083d3d8);
          FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        }
        if (lVar3 == 0) {
          return;
        }
        lVar3 = FUN_ffff00009077f250(&local_98);
        (&uStack_99)[lVar3] = 0x62;
        uVar5 = FUN_ffff00009070e1a8(&local_98);
        if ((uVar5 & 1) != 0) {
          if (((DAT_ffff000090816d70 != '\0') && (_DAT_ffff00009083dfc8 != 0)) &&
             ((iVar1 = FUN_ffff00009077f394(s_system_ffff0000907edad2,0xffff00009083d3d8,0x20),
              iVar1 == 0 ||
              (iVar1 = FUN_ffff00009077f394(s_super_ffff0000907e5b8c,0xffff00009083d3d8,0x20),
              iVar1 == 0)))) {
            FUN_ffff000090709bec(0xffff00009083d3d8);
            FUN_ffff000090709cdc(&DAT_ffff0000907df338);
            return;
          }
          if (*param_2 == -0x12d900c6) {
            FUN_ffff00009070e7ac();
            return;
          }
          FUN_ffff00009070e424(&local_98,param_2,param_3);
          return;
        }
      }
      pcVar6 = s_partition_is_not_writable_ffff0000907d3e8b;
    }
  }
  else {
    uVar2 = FUN_ffff00009077f250(s_nvram_ffff0000907ef456);
    iVar1 = FUN_ffff00009077f348(param_1,s_nvram_ffff0000907ef456,uVar2);
    if (iVar1 != 0) {
      uVar2 = FUN_ffff00009077f250(s_nvcfg_ffff0000907ea6f5);
      iVar1 = FUN_ffff00009077f348(param_1,s_nvcfg_ffff0000907ea6f5,uVar2);
      if (iVar1 != 0) {
        uVar2 = FUN_ffff00009077f250(s_proinfo_ffff0000907e8d23);
        iVar1 = FUN_ffff00009077f348(param_1,s_proinfo_ffff0000907e8d23,uVar2);
        if (iVar1 != 0) {
          uVar2 = FUN_ffff00009077f250(s_nvdata_ffff0000907fea9f);
          iVar1 = FUN_ffff00009077f348(param_1,s_nvdata_ffff0000907fea9f,uVar2);
          if (iVar1 != 0) {
            uVar2 = FUN_ffff00009077f250(s_protect1_ffff0000907edb0b);
            iVar1 = FUN_ffff00009077f348(param_1,s_protect1_ffff0000907edb0b,uVar2);
            if (iVar1 != 0) {
              uVar2 = FUN_ffff00009077f250(s_protect2_ffff0000907da588);
              iVar1 = FUN_ffff00009077f348(param_1,s_protect2_ffff0000907da588,uVar2);
              if (iVar1 != 0) {
                uVar2 = FUN_ffff00009077f250(s_persist_ffff0000907e74b2);
                iVar1 = FUN_ffff00009077f348(param_1,s_persist_ffff0000907e74b2,uVar2);
                if (iVar1 != 0) {
                  uVar2 = FUN_ffff00009077f250(s_oem_misc1_ffff0000907d5809);
                  iVar1 = FUN_ffff00009077f348(param_1,s_oem_misc1_ffff0000907d5809,uVar2);
                  if (iVar1 != 0) {
                    uVar2 = FUN_ffff00009077f250(s_blackbox_ffff0000907f0d8c);
                    iVar1 = FUN_ffff00009077f348(param_1,s_blackbox_ffff0000907f0d8c,uVar2);
                    if (iVar1 != 0) {
                      uVar2 = FUN_ffff00009077f250(s_display_dbi_ffff0000907e252f);
                      iVar1 = FUN_ffff00009077f348(param_1,s_display_dbi_ffff0000907e252f,uVar2);
                      if (iVar1 != 0) goto LAB_ffff00009070cff8;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    pcVar6 = s_Flashing_is_not_allowed_for_Prot_ffff0000907e0b21;
  }
LAB_ffff00009070d1b0:
  FUN_ffff000090709c9c(pcVar6);
  return;
}


