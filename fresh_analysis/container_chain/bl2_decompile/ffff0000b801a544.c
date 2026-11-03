
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b801a544(uint param_1)

{
  uint uVar1;
  uint uVar2;
  ushort uVar3;
  int iVar4;
  char *pcVar5;
  undefined8 uVar6;
  
  uVar2 = *(uint *)(&DAT_ffff0000b80e8dc0 + (ulong)param_1 * 4);
  if ((int)uVar2 < 0x48) {
    switch(param_1) {
    case 1:
    case 8:
      if (DAT_ffff0000b811b24c == 2) {
        iVar4 = FUN_ffff0000b801ffc8(2);
        if (iVar4 == 1) {
          pcVar5 = s__s__d__homekey2_key_is_pressed__k_ffff0000b808b79b;
          uVar6 = 0xe3;
LAB_ffff0000b801a6f0:
          FUN_ffff0000b804c33c(pcVar5,s_detect_key_ffff0000b8096313,uVar6,uVar2);
          return 1;
        }
      }
      else if (DAT_ffff0000b811b24c == 1) {
        iVar4 = FUN_ffff0000b8050948(DAT_ffff0000b811b248);
        if (iVar4 == 0) {
          pcVar5 = s__s__d__gpio_key_is_pressed__key__ffff0000b80852c5;
          uVar6 = 0xdd;
          goto LAB_ffff0000b801a6f0;
        }
      }
      else if (DAT_ffff0000b811b24c == 0) {
        uVar1 = uVar2 + 0xf;
        if (-1 < (int)uVar2) {
          uVar1 = uVar2;
        }
        uVar3 = *(ushort *)
                 (&DAT_ffff00001c012004 +
                 (-(ulong)((uint)((int)uVar1 >> 4) >> 0x1f) & 0xfffffffc00000000 |
                 (ulong)(uint)((int)uVar1 >> 4) << 2));
        FUN_ffff0000b804c33c
                  (s__s__d__KP_STA__0x_x__KP_MEM1__0x_ffff0000b808fadd,s_detect_key_ffff0000b8096313
                   ,0xd5,_DAT_ffff00001c012000,_DAT_ffff00001c012004);
        if ((uVar3 >> (ulong)(uVar2 & 0xf) & 1) == 0) {
          pcVar5 = s__s__d__kpd_ip_key_is_pressed__ke_ffff0000b8099baf;
          uVar6 = 0xd7;
          goto LAB_ffff0000b801a6f0;
        }
      }
      else {
        FUN_ffff0000b804c33c
                  (s__s__d__no_kpd_factory_key_type_s_ffff0000b809e3fa,s_detect_key_ffff0000b8096313
                   ,0xe8,uVar2);
      }
      break;
    case 2:
      iVar4 = FUN_ffff0000b801ffc8(0);
      if (iVar4 != 1) {
        return 0;
      }
      pcVar5 = s__s__d__power_key_is_pressed_ffff0000b80917c3;
      uVar6 = 0xbe;
LAB_ffff0000b801a698:
      FUN_ffff0000b804c33c(pcVar5,s_detect_key_ffff0000b8096313,uVar6);
      return 1;
    case 3:
    case 6:
      iVar4 = FUN_ffff0000b801ffc8(1);
      if (iVar4 == 1) {
        pcVar5 = s__s__d__home_key_is_pressed_ffff0000b8088d89;
        uVar6 = 199;
        goto LAB_ffff0000b801a698;
      }
    }
  }
  return 0;
}


