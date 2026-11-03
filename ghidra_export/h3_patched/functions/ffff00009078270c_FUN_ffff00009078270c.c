// ffff00009078270c  size=1252  FUN_ffff00009078270c

void FUN_ffff00009078270c(long param_1)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  undefined *puVar4;
  undefined *puVar5;
  int *piVar6;
  char *pcVar7;
  char *pcVar8;
  undefined8 uVar9;
  long lVar10;
  undefined4 local_54;
  char local_50 [8];
  char acStack_48 [8];
  char local_40 [6];
  char acStack_3a [2];
  char acStack_38 [8];
  
  local_50 = (char  [8])s_mediatek__mrdump_ext_rst_eint_ffff0000907fe089._0_8_;
  acStack_48 = (char  [8])s_mediatek__mrdump_ext_rst_eint_ffff0000907fe089._8_8_;
  local_40 = (char  [6])s_mediatek__mrdump_ext_rst_eint_ffff0000907fe089._16_6_;
  acStack_3a = (char  [2])s_mediatek__mrdump_ext_rst_eint_ffff0000907fe089._22_2_;
  acStack_38._0_6_ = s_mediatek__mrdump_ext_rst_eint_ffff0000907fe089._24_6_;
  if (param_1 == 0) {
    FUN_ffff00009077de54
              (s__s__d__fdt_is_NULL_ffff0000907f7c01,s_mrdump_key_fdt_init_ffff0000907f8c2f,0x84);
    return;
  }
  local_54 = 0;
  iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,local_50);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d___s_not_found_ffff0000907fc33a,s_mrdump_key_fdt_init_ffff0000907f8c2f,0x8a,
               local_50);
    return;
  }
  puVar4 = (undefined *)FUN_ffff00009076dcd4(param_1,iVar2,s_force_mode_ffff0000907ed08e,&local_54);
  if (puVar4 == (undefined *)0x0) {
    pcVar7 = s__s__d__no_force_mode_attribute_d_ffff0000907e53ba;
    puVar4 = &DAT_ffff0000907fe0a7;
    uVar9 = 0x93;
  }
  else {
    pcVar7 = s__s__d__force_mode____s_ffff0000907e0281;
    uVar9 = 0x90;
  }
  FUN_ffff00009077de54(pcVar7,s_mrdump_key_fdt_init_ffff0000907f8c2f,uVar9,puVar4);
  puVar5 = (undefined *)FUN_ffff00009076dcd4(param_1,iVar2,&DAT_ffff0000907da53c,&local_54);
  if (puVar5 == (undefined *)0x0) {
    pcVar7 = s__s__d__no_mode_attribute_default_ffff0000907e1e7e;
    puVar5 = &DAT_ffff0000907eb6c9;
    uVar9 = 0x9a;
  }
  else {
    pcVar7 = s__s__d__mode____s_ffff0000907d6bbf;
    uVar9 = 0x97;
  }
  FUN_ffff00009077de54(pcVar7,s_mrdump_key_fdt_init_ffff0000907f8c2f,uVar9,puVar5);
  piVar6 = (int *)FUN_ffff00009076dcd4(param_1,iVar2,s_interrupts_ffff0000907f8c43,&local_54);
  if (piVar6 == (int *)0x0) {
    uVar9 = FUN_ffff00009077ef88(s_SYSRST_ffff0000907eb6cd);
    iVar2 = FUN_ffff00009077f080(s_SYSRST_ffff0000907eb6cd,puVar4,uVar9);
    if (iVar2 != 0) {
      pcVar7 = s__s__d__Error__no_interrupts_attr_ffff0000907ffbd3;
      pcVar8 = s_mrdump_key_fdt_init_ffff0000907f8c2f;
      uVar9 = 0xab;
      goto LAB_ffff000090782970;
    }
    FUN_ffff00009077de54
              (s__s__d__Ignore_interrupts_attribu_ffff0000907e9e62,
               s_mrdump_key_fdt_init_ffff0000907f8c2f,0xa8);
    iVar2 = 0;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__interrupts___0x_x_ffff0000907fa707,s_mrdump_key_fdt_init_ffff0000907f8c2f,
               0x9e,*piVar6);
    iVar3 = *piVar6;
    iVar2 = iVar3 + 0xffffff;
    if (-1 < iVar3) {
      iVar2 = iVar3;
    }
    iVar2 = iVar2 >> 0x18;
    FUN_ffff00009077de54
              (s__s__d__mrdump_key_pin___EINT_d_ffff0000907f7124,
               s_mrdump_key_fdt_init_ffff0000907f8c2f,0xa2,iVar2);
  }
  uVar9 = FUN_ffff00009077ef88(s_SYSRST_ffff0000907eb6cd);
  iVar3 = FUN_ffff00009077f080(s_SYSRST_ffff0000907eb6cd,puVar4,uVar9);
  if (iVar3 == 0) {
    uVar9 = FUN_ffff00009077ef88(&DAT_ffff0000907eb6c9);
    iVar2 = FUN_ffff00009077f080(puVar5,&DAT_ffff0000907eb6c9,uVar9);
    if (iVar2 == 0) {
      uVar9 = 0;
    }
    else {
      uVar9 = FUN_ffff00009077ef88(&DAT_ffff000090802cf4);
      iVar2 = FUN_ffff00009077f080(puVar5,&DAT_ffff000090802cf4,uVar9);
      if (iVar2 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__bad_rst_mode__s_ffff0000907d01a9,s_mrdump_key_apply_ffff0000907e0299,
                   0x60,puVar5);
        return;
      }
      uVar9 = 1;
    }
    FUN_ffff000090746030(1,uVar9);
    FUN_ffff000090736270(1);
    FUN_ffff00009073630c(1);
    return;
  }
  uVar9 = FUN_ffff00009077ef88(&DAT_ffff0000907fe0a7);
  iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907fe0a7,puVar4,uVar9);
  if (iVar3 == 0) {
    FUN_ffff000090785720(iVar2,1);
    FUN_ffff000090785354(iVar2,0);
    FUN_ffff000090785468(iVar2,0);
    lVar10 = 0;
    switch(iVar2) {
    case 7:
      lVar10 = 9;
      break;
    case 8:
      lVar10 = 8;
      break;
    default:
      FUN_ffff00009077de54
                (s__s__d__can_t_get_rgu_pin_for_ein_ffff0000907ed099,
                 s_select_rgp_pin_ffff0000907db7c4,0x41,iVar2);
      return;
    case 0xb:
      lVar10 = 7;
      break;
    case 0xd:
      lVar10 = 0xe;
      break;
    case 0x27:
      lVar10 = 6;
      break;
    case 0x28:
      lVar10 = 5;
      break;
    case 0x29:
      lVar10 = 4;
      break;
    case 0x2a:
      lVar10 = 3;
      break;
    case 0x2b:
      lVar10 = 2;
      break;
    case 0x5c:
      lVar10 = 1;
      break;
    case 0x5d:
      break;
    case 0x72:
      lVar10 = 0xd;
      break;
    case 0x73:
      lVar10 = 0xc;
      break;
    case 0x74:
      lVar10 = 0xb;
      break;
    case 0x75:
      lVar10 = 10;
    }
    uVar1 = (&DAT_ffff000090811f20)[lVar10 * 2];
    FUN_ffff00009077de54
              (s__s__d__get_rgu_pin__d__for_eint__ffff0000907f0456,s_select_rgp_pin_ffff0000907db7c4
               ,0x3d,uVar1,iVar2);
    uVar9 = FUN_ffff00009077ef88(&DAT_ffff0000907eb6c9);
    iVar2 = FUN_ffff00009077f080(puVar5,&DAT_ffff0000907eb6c9,uVar9);
    if (iVar2 == 0) {
      uVar9 = 0;
    }
    else {
      uVar9 = FUN_ffff00009077ef88(&DAT_ffff000090802cf4);
      iVar2 = FUN_ffff00009077f080(puVar5,&DAT_ffff000090802cf4,uVar9);
      if (iVar2 != 0) {
        return;
      }
      uVar9 = 1;
    }
    FUN_ffff000090745fb8(1,uVar9,uVar1);
    return;
  }
  pcVar7 = s__s__d__no_valid_trigger_info_ffff0000907d50ea;
  pcVar8 = s_mrdump_key_apply_ffff0000907e0299;
  uVar9 = 0x6e;
LAB_ffff000090782970:
  FUN_ffff00009077de54(pcVar7,pcVar8,uVar9);
  return;
}


