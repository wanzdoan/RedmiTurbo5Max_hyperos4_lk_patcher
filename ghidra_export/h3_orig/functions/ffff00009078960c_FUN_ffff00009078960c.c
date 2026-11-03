// ffff00009078960c  size=544  FUN_ffff00009078960c

int FUN_ffff00009078960c(undefined8 param_1,undefined4 param_2)

{
  char cVar1;
  int iVar2;
  char *pcVar3;
  char *pcVar4;
  char local_a48 [4];
  char local_a44 [4];
  undefined8 local_a40;
  undefined8 uStack_a38;
  byte abStack_a30 [4];
  char local_a2c [2044];
  undefined1 auStack_230 [512];
  
  FUN_ffff00009077eb88(auStack_230,0,0x200);
  FUN_ffff00009077eb88(abStack_a30,0,0x800);
  local_a40 = 0;
  uStack_a38 = 0;
  local_a44[0] = '\0';
  local_a48[0] = '\0';
  iVar2 = FUN_ffff000090789244(param_1,param_2,auStack_230);
  if (iVar2 < 1) {
    FUN_ffff00009077de54(s_token_sig_decrypt_failed__d_ffff0000907ed210,iVar2);
    return iVar2;
  }
  iVar2 = FUN_ffff000090789474(auStack_230,abStack_a30);
  abStack_a30[iVar2] = 0;
  FUN_ffff000090788dc8();
  pcVar3 = &DAT_ffff00009088750c;
  do {
    if (*pcVar3 == '\x02') {
      FUN_ffff00009077de54(s__unlock_token_type__d_found___le_ffff0000907ffc91,2);
      cVar1 = pcVar3[1];
      pcVar4 = local_a2c;
      goto LAB_ffff000090789708;
    }
    pcVar4 = pcVar3 + (ulong)(byte)pcVar3[1] + 0xffff6f778af6;
    pcVar3 = pcVar3 + (ulong)(byte)pcVar3[1] + 2;
  } while (pcVar4 < (char *)(ulong)DAT_ffff00009088750b);
  goto LAB_ffff00009078972c;
  while (pcVar4 = pcVar4 + (ulong)(byte)pcVar4[1] + 2,
        (ulong)((long)pcVar4 - (long)local_a2c) < (ulong)abStack_a30[3]) {
LAB_ffff000090789708:
    if (*pcVar4 == '\x02') {
      FUN_ffff00009077de54(s__unlock_token_type__d_found___le_ffff0000907ffc91,2);
      if (cVar1 != pcVar4[1]) {
        pcVar3 = s_cpuid_len_mismatch___d__d_ffff0000907ea016;
LAB_ffff0000907897a8:
        FUN_ffff00009077de54(pcVar3,cVar1);
        return -2;
      }
      iVar2 = FUN_ffff00009077e820(pcVar3 + 2,pcVar4 + 2,cVar1);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907893a4(&DAT_ffff000090887508,&uStack_a38,local_a44,3);
        if (iVar2 != 0) {
          return -1;
        }
        iVar2 = FUN_ffff0000907893a4(abStack_a30,&local_a40,local_a48,3);
        if (iVar2 != 0) {
          return -1;
        }
        if (local_a44[0] != local_a48[0]) {
          pcVar3 = s_device_name_len_mismatch___d__d_ffff0000907ed22d;
          cVar1 = local_a44[0];
          goto LAB_ffff0000907897a8;
        }
        iVar2 = FUN_ffff00009077e820(uStack_a38,local_a40,local_a44[0]);
        if (iVar2 == 0) {
          return 0;
        }
        pcVar3 = s_device_name_mismatch_ffff0000907d85d1;
      }
      else {
        pcVar3 = s_cpuid_mismatch_ffff0000907f36ee;
      }
      FUN_ffff00009077de54(pcVar3);
      return -3;
    }
  }
LAB_ffff00009078972c:
  FUN_ffff00009077de54(s__unlock_token_no_token_found__ffff0000907d1f38);
  return -1;
}


