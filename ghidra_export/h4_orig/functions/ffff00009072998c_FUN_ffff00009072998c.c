// ffff00009072998c  size=476  FUN_ffff00009072998c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009072998c(uint param_1)

{
  uint uVar1;
  uint uVar2;
  ushort uVar3;
  int iVar4;
  char *pcVar5;
  undefined8 uVar6;
  
  uVar2 = (&DAT_ffff000090818e28)[param_1];
  if ((int)uVar2 < 0x48) {
    switch(param_1) {
    case 1:
    case 8:
      if (_DAT_ffff00009086d578 == 2) {
        iVar4 = FUN_ffff000090736330(2);
        if (iVar4 == 1) {
          pcVar5 = s__s__d__homekey2_key_is_pressed__k_ffff0000907da9e5;
          uVar6 = 0xe3;
LAB_ffff000090729b38:
          FUN_ffff00009077e11c(pcVar5,s_detect_key_ffff0000907edfea,uVar6,uVar2);
          return 1;
        }
      }
      else if (_DAT_ffff00009086d578 == 1) {
        iVar4 = FUN_ffff0000907856ec(_DAT_ffff00009086d574);
        if (iVar4 == 0) {
          pcVar5 = s__s__d__gpio_key_is_pressed__key__ffff0000907cf4f2;
          uVar6 = 0xdd;
          goto LAB_ffff000090729b38;
        }
      }
      else if (_DAT_ffff00009086d578 == 0) {
        uVar1 = uVar2 + 0xf;
        if (-1 < (int)uVar2) {
          uVar1 = uVar2;
        }
        uVar3 = *(ushort *)
                 (&DAT_ffff00001c012004 +
                 (-(ulong)((uint)((int)uVar1 >> 4) >> 0x1f) & 0xfffffffc00000000 |
                 (ulong)(uint)((int)uVar1 >> 4) << 2));
        FUN_ffff00009077e11c
                  (s__s__d__KP_STA__0x_x__KP_MEM1__0x_ffff0000907e29e9,s_detect_key_ffff0000907edfea
                   ,0xd5,_DAT_ffff00001c012000,_DAT_ffff00001c012004);
        if ((uVar3 >> (ulong)(uVar2 & 0xf) & 1) == 0) {
          pcVar5 = s__s__d__kpd_ip_key_is_pressed__ke_ffff0000907f463c;
          uVar6 = 0xd7;
          goto LAB_ffff000090729b38;
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__no_kpd_factory_key_type_s_ffff0000907fd08c,s_detect_key_ffff0000907edfea
                   ,0xe8,uVar2);
      }
      break;
    case 2:
      iVar4 = FUN_ffff000090736330(0);
      if (iVar4 != 1) {
        return 0;
      }
      pcVar5 = s__s__d__power_key_is_pressed_ffff0000907e6029;
      uVar6 = 0xbe;
LAB_ffff000090729ae0:
      FUN_ffff00009077e11c(pcVar5,s_detect_key_ffff0000907edfea,uVar6);
      return 1;
    case 3:
    case 6:
      iVar4 = FUN_ffff000090736330(1);
      if (iVar4 == 1) {
        pcVar5 = s__s__d__home_key_is_pressed_ffff0000907d5ddb;
        uVar6 = 199;
        goto LAB_ffff000090729ae0;
      }
    }
  }
  return 0;
}


