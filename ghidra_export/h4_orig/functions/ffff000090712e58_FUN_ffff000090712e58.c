// ffff000090712e58  size=272  FUN_ffff000090712e58

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090712e58(char param_1)

{
  char *pcVar1;
  uint uVar2;
  uint uVar3;
  
  uVar3 = _DAT_ffff00009083f83c >> 2 & 1;
  uVar2 = _DAT_ffff00009083f83c & 1 ^ 1;
  if (param_1 == '\x04') {
    pcVar1 = s_androidboot_usbconfig_0_usbconfi_ffff0000907dc09b;
    if ((_DAT_ffff00009083f83c & 1) != 0) {
      pcVar1 = s_androidboot_usbconfig_1_usbconfi_ffff0000907e8df5;
    }
    FUN_ffff000090719078(pcVar1);
    FUN_ffff00009077e11c
              (s__s__d__adb__d_elt__d_ffff0000907d3f97,s_set_usbconfig_ffff0000907dc0bf,0x53,uVar2,
               uVar3);
  }
  else if (param_1 == '\x01') {
    FUN_ffff000090719078
              (&DAT_ffff0000908044b8 +
               *(int *)(&DAT_ffff0000908044b8 + ((ulong)_DAT_ffff00009083f83c & 5) * 4));
    pcVar1 = s_androidboot_mblogenable_1_ffff0000907e4313;
    if ((_DAT_ffff00009083f83c & 2) != 0) {
      pcVar1 = s_androidboot_mblogenable_0_ffff0000907d0b5b;
    }
    FUN_ffff000090719078(pcVar1);
    FUN_ffff00009077e11c
              (s__s__d__adb__d_elt__d_meta_com_id_ffff0000907d3f72,s_set_usbconfig_ffff0000907dc0bf,
               0x4c,uVar2,uVar3,_DAT_ffff00009083f83c);
  }
  pcVar1 = s_androidboot_meta_log_disable_1_ffff0000907dd9d4;
  if (_DAT_ffff00009083f844 != 1) {
    pcVar1 = s_androidboot_meta_log_disable_0_ffff0000907fb1cc;
  }
  FUN_ffff000090719078(pcVar1);
  return;
}


