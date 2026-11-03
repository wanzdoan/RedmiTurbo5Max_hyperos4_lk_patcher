// ffff00009070cd88  size=1016  FUN_ffff00009070cd88

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070cd88(undefined8 *param_1,int *param_2,undefined4 param_3)

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
  iVar1 = thunk_FUN_ffff0000907b5f54(&local_34);
  if (iVar1 != 0) {
    pcVar6 = s_security_deny_ffff0000907e2439;
    goto LAB_ffff00009070d138;
  }
  if ((local_34 == 1) &&
     (iVar1 = FUN_ffff00009077ede4(s_boot_para_ffff0000907f7a23,param_1), iVar1 != 0)) {
    pcVar6 = s_Flashing_is_not_allowed_in_Lock_s_ffff0000907dbea3;
    goto LAB_ffff00009070d138;
  }
  if (param_1 == (undefined8 *)0x0) {
LAB_ffff00009070cf80:
    lVar3 = FUN_ffff00009077ee0c(&local_98,param_1);
    if (lVar3 == 0) {
      FUN_ffff00009077de54(s_strcpy_partition_name_from_arg_f_ffff0000907fca0d);
      pcVar6 = s_unknow_reason_ffff0000907d3e40;
    }
    else {
      lVar3 = FUN_ffff00009077f27c(&local_98,&DAT_ffff0000907f0c00);
      if (lVar3 != 0) {
        lVar4 = FUN_ffff00009077ef88(&local_98);
        (&uStack_99)[lVar4] = 0;
        param_1 = &local_98;
        lVar4 = FUN_ffff00009077ef88(&local_98);
        (&uStack_99)[lVar4] = 0x61;
      }
      uVar5 = FUN_ffff00009070e130(param_1);
      if ((uVar5 & 1) != 0) {
        if (((DAT_ffff000090816d60 == '\0') || (_DAT_ffff00009083df90 == 0)) ||
           ((iVar1 = FUN_ffff00009077f0cc(s_system_ffff0000907ed94a,0xffff00009083d39c,0x20),
            iVar1 != 0 &&
            (iVar1 = FUN_ffff00009077f0cc(s_super_ffff0000907e5a61,0xffff00009083d39c,0x20),
            iVar1 != 0)))) {
          if (*param_2 == -0x12d900c6) {
            FUN_ffff00009070e734();
          }
          else {
            FUN_ffff00009070e3ac(param_1,param_2,param_3);
          }
        }
        else {
          FUN_ffff000090709c60(0xffff00009083d39c);
          FUN_ffff000090709d50(&DAT_ffff0000907df27e);
        }
        if (lVar3 == 0) {
          return;
        }
        lVar3 = FUN_ffff00009077ef88(&local_98);
        (&uStack_99)[lVar3] = 0x62;
        uVar5 = FUN_ffff00009070e130(&local_98);
        if ((uVar5 & 1) != 0) {
          if (((DAT_ffff000090816d60 != '\0') && (_DAT_ffff00009083df90 != 0)) &&
             ((iVar1 = FUN_ffff00009077f0cc(s_system_ffff0000907ed94a,0xffff00009083d39c,0x20),
              iVar1 == 0 ||
              (iVar1 = FUN_ffff00009077f0cc(s_super_ffff0000907e5a61,0xffff00009083d39c,0x20),
              iVar1 == 0)))) {
            FUN_ffff000090709c60(0xffff00009083d39c);
            FUN_ffff000090709d50(&DAT_ffff0000907df27e);
            return;
          }
          if (*param_2 == -0x12d900c6) {
            FUN_ffff00009070e734();
            return;
          }
          FUN_ffff00009070e3ac(&local_98,param_2,param_3);
          return;
        }
      }
      pcVar6 = s_partition_is_not_writable_ffff0000907d3e4e;
    }
  }
  else {
    uVar2 = FUN_ffff00009077ef88(s_nvram_ffff0000907ef2ce);
    iVar1 = FUN_ffff00009077f080(param_1,s_nvram_ffff0000907ef2ce,uVar2);
    if (iVar1 != 0) {
      uVar2 = FUN_ffff00009077ef88(s_nvcfg_ffff0000907ea584);
      iVar1 = FUN_ffff00009077f080(param_1,s_nvcfg_ffff0000907ea584,uVar2);
      if (iVar1 != 0) {
        uVar2 = FUN_ffff00009077ef88(s_proinfo_ffff0000907e8bc8);
        iVar1 = FUN_ffff00009077f080(param_1,s_proinfo_ffff0000907e8bc8,uVar2);
        if (iVar1 != 0) {
          uVar2 = FUN_ffff00009077ef88(s_nvdata_ffff0000907fe8f7);
          iVar1 = FUN_ffff00009077f080(param_1,s_nvdata_ffff0000907fe8f7,uVar2);
          if (iVar1 != 0) {
            uVar2 = FUN_ffff00009077ef88(s_protect1_ffff0000907ed983);
            iVar1 = FUN_ffff00009077f080(param_1,s_protect1_ffff0000907ed983,uVar2);
            if (iVar1 != 0) {
              uVar2 = FUN_ffff00009077ef88(s_protect2_ffff0000907da4da);
              iVar1 = FUN_ffff00009077f080(param_1,s_protect2_ffff0000907da4da,uVar2);
              if (iVar1 != 0) {
                uVar2 = FUN_ffff00009077ef88(s_persist_ffff0000907e7357);
                iVar1 = FUN_ffff00009077f080(param_1,s_persist_ffff0000907e7357,uVar2);
                if (iVar1 != 0) {
                  uVar2 = FUN_ffff00009077ef88(s_oem_misc1_ffff0000907d57b9);
                  iVar1 = FUN_ffff00009077f080(param_1,s_oem_misc1_ffff0000907d57b9,uVar2);
                  if (iVar1 != 0) {
                    uVar2 = FUN_ffff00009077ef88(s_blackbox_ffff0000907f0c04);
                    iVar1 = FUN_ffff00009077f080(param_1,s_blackbox_ffff0000907f0c04,uVar2);
                    if (iVar1 != 0) {
                      uVar2 = FUN_ffff00009077ef88(s_display_dbi_ffff0000907e2447);
                      iVar1 = FUN_ffff00009077f080(param_1,s_display_dbi_ffff0000907e2447,uVar2);
                      if (iVar1 != 0) goto LAB_ffff00009070cf80;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    pcVar6 = s_Flashing_is_not_allowed_for_Prot_ffff0000907e0a67;
  }
LAB_ffff00009070d138:
  FUN_ffff000090709d10(pcVar6);
  return;
}


