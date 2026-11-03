// ffff000090705c34  size=704  FUN_ffff000090705c34

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090705c34(void)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  char *pcVar6;
  ulong uVar7;
  undefined8 uVar8;
  undefined *puVar9;
  ulong uVar10;
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
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  uint local_44;
  
  _DAT_ffff00009083d0e8 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (_DAT_ffff00009083d0e8 == 0) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_get_mt6379_regm_ffff0000907ebc13,
               s_mt6379_dlpt_init_ffff0000907ebc37,0x151);
    return;
  }
  local_44 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
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
  FUN_ffff00009077de54
            (s__s__d___s__gogo3_ffff000090801bb8,s_mt6379_save_debug_info_cmdline_ffff0000907f9084,
             0x11f,s_mt6379_save_debug_info_cmdline_ffff0000907f9084);
  iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,0x52,&local_44);
  if (iVar4 == 0) {
    FUN_ffff000090737284(_DAT_ffff00009083d0e8,0x52,local_44);
    uVar5 = local_44;
    iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,0x5c,&local_44);
    if (iVar4 == 0) {
      FUN_ffff000090737284(_DAT_ffff00009083d0e8,0x5c,local_44);
      uVar2 = local_44;
      iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,100,&local_44);
      if (iVar4 == 0) {
        FUN_ffff000090737284(_DAT_ffff00009083d0e8,100,local_44);
        uVar3 = local_44;
        iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,0x121,&local_44);
        if (iVar4 == 0) {
          FUN_ffff000090737284(_DAT_ffff00009083d0e8,0x121,local_44);
          uVar1 = uVar3 >> 2;
          uVar5 = FUN_ffff00009077f434
                            (&local_c8,0x80,s_MT6379_EVT____02x_ffff0000907ed86a,
                             uVar2 >> 2 & 2 | uVar5 & 1 | uVar3 & 8 | uVar1 & 4 | (uVar1 & 1) << 4 |
                             (local_44 >> 4 & 1) << 5);
          if ((int)uVar5 < 0) {
            FUN_ffff00009077de54
                      (s__s__d__Failed_to_generate_MT6379_ffff0000907e3f95,
                       s_mt6379_save_debug_info_cmdline_ffff0000907f9084,0x13f);
          }
          else if (uVar5 < 0x80) {
            FUN_ffff00009077de54
                      (s__s__d___s_ffff0000907ef190,
                       s_mt6379_save_debug_info_cmdline_ffff0000907f9084,0x143,&local_c8);
            FUN_ffff000090718f34(&local_c8);
          }
          else {
            FUN_ffff00009077de54
                      (s__s__d__MT6379_EVT_string_buffer_o_ffff0000907d241b,
                       s_mt6379_save_debug_info_cmdline_ffff0000907f9084,0x141);
          }
          goto LAB_ffff000090705d4c;
        }
      }
    }
  }
  FUN_ffff00009077de54
            (s__s__d__Failed_to_save_debug_info_ffff0000907df1da,s_mt6379_dlpt_init_ffff0000907ebc37
             ,0x158);
LAB_ffff000090705d4c:
  iVar4 = FUN_ffff00009074124c();
  if (iVar4 != 0) {
    uVar10 = 0;
    puVar9 = &DAT_ffff000090816880;
    do {
      iVar4 = FUN_ffff000090736ea8(puVar9);
      if (iVar4 == 0) {
        pcVar6 = s__s__d__Register__s_dlpt_device_s_ffff0000907e08e5;
        uVar8 = 0x160;
      }
      else {
        uVar8 = 0x15e;
        pcVar6 = s__s__d__Failed_to_register__s_dlp_ffff0000907f7818;
      }
      FUN_ffff00009077de54
                (pcVar6,s_mt6379_dlpt_init_ffff0000907ebc37,uVar8,
                 (&PTR_s_mt6379_bat1_ffff0000908168b0)[uVar10]);
      uVar10 = uVar10 + 1;
      uVar7 = FUN_ffff00009074124c();
      puVar9 = puVar9 + 0x18;
    } while (uVar10 < (uVar7 & 0xffffffff));
  }
  FUN_ffff00009077de54
            (s__s__d__LK_init_done__ffff0000907f3c5b,s_mt6379_dlpt_init_ffff0000907ebc37,0x163);
  return;
}


