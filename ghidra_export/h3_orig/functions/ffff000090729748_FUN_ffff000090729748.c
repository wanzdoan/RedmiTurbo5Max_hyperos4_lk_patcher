// ffff000090729748  size=476  FUN_ffff000090729748

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090729748(uint param_1)

{
  uint uVar1;
  uint uVar2;
  ushort uVar3;
  int iVar4;
  char *pcVar5;
  undefined8 uVar6;
  
  uVar2 = (&DAT_ffff000090818e08)[param_1];
  if ((int)uVar2 < 0x48) {
    switch(param_1) {
    case 1:
    case 8:
      if (_DAT_ffff00009086d4e0 == 2) {
        iVar4 = FUN_ffff000090736098(2);
        if (iVar4 == 1) {
          pcVar5 = s__s__d__homekey2_key_is_pressed__k_ffff0000907da8da;
          uVar6 = 0xe3;
LAB_ffff0000907298f4:
          FUN_ffff00009077de54(pcVar5,s_detect_key_ffff0000907ede79,uVar6,uVar2);
          return 1;
        }
      }
      else if (_DAT_ffff00009086d4e0 == 1) {
        iVar4 = FUN_ffff000090785424(_DAT_ffff00009086d4dc);
        if (iVar4 == 0) {
          pcVar5 = s__s__d__gpio_key_is_pressed__key__ffff0000907cf4d3;
          uVar6 = 0xdd;
          goto LAB_ffff0000907298f4;
        }
      }
      else if (_DAT_ffff00009086d4e0 == 0) {
        uVar1 = uVar2 + 0xf;
        if (-1 < (int)uVar2) {
          uVar1 = uVar2;
        }
        uVar3 = *(ushort *)
                 (&DAT_ffff00001c012004 +
                 (-(ulong)((uint)((int)uVar1 >> 4) >> 0x1f) & 0xfffffffc00000000 |
                 (ulong)(uint)((int)uVar1 >> 4) << 2));
        FUN_ffff00009077de54
                  (s__s__d__KP_STA__0x_x__KP_MEM1__0x_ffff0000907e2918,s_detect_key_ffff0000907ede79
                   ,0xd5,_DAT_ffff00001c012000,_DAT_ffff00001c012004);
        if ((uVar3 >> (ulong)(uVar2 & 0xf) & 1) == 0) {
          pcVar5 = s__s__d__kpd_ip_key_is_pressed__ke_ffff0000907f44cf;
          uVar6 = 0xd7;
          goto LAB_ffff0000907298f4;
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d__no_kpd_factory_key_type_s_ffff0000907fcee4,s_detect_key_ffff0000907ede79
                   ,0xe8,uVar2);
      }
      break;
    case 2:
      iVar4 = FUN_ffff000090736098(0);
      if (iVar4 != 1) {
        return 0;
      }
      pcVar5 = s__s__d__power_key_is_pressed_ffff0000907e5f15;
      uVar6 = 0xbe;
LAB_ffff00009072989c:
      FUN_ffff00009077de54(pcVar5,s_detect_key_ffff0000907ede79,uVar6);
      return 1;
    case 3:
    case 6:
      iVar4 = FUN_ffff000090736098(1);
      if (iVar4 == 1) {
        pcVar5 = s__s__d__home_key_is_pressed_ffff0000907d5d45;
        uVar6 = 199;
        goto LAB_ffff00009072989c;
      }
    }
  }
  return 0;
}


