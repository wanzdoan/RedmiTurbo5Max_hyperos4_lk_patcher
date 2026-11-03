// ffff000090797348  size=276  FUN_ffff000090797348

undefined1 * FUN_ffff000090797348(undefined1 *param_1,long param_2)

{
  char cVar1;
  ulong uVar2;
  uint uVar3;
  long lVar4;
  ulong uVar5;
  char *pcVar6;
  char *pcVar7;
  char local_48 [40];
  
  lVar4 = 0;
  local_48[0x20] = '\0';
  local_48[0x21] = '\0';
  local_48[0x22] = '\0';
  local_48[0x23] = '\0';
  local_48[0x24] = '\0';
  local_48[0x10] = '\0';
  local_48[0x11] = '\0';
  local_48[0x12] = '\0';
  local_48[0x13] = '\0';
  local_48[0x14] = '\0';
  local_48[0x15] = '\0';
  local_48[0x16] = '\0';
  local_48[0x17] = '\0';
  local_48[0x18] = '\0';
  local_48[0x19] = '\0';
  local_48[0x1a] = '\0';
  local_48[0x1b] = '\0';
  local_48[0x1c] = '\0';
  local_48[0x1d] = '\0';
  local_48[0x1e] = '\0';
  local_48[0x1f] = '\0';
  local_48[0] = '\0';
  local_48[1] = '\0';
  local_48[2] = '\0';
  local_48[3] = '\0';
  local_48[4] = '\0';
  local_48[5] = '\0';
  local_48[6] = '\0';
  local_48[7] = '\0';
  local_48[8] = '\0';
  local_48[9] = '\0';
  local_48[10] = '\0';
  local_48[0xb] = '\0';
  local_48[0xc] = '\0';
  local_48[0xd] = '\0';
  local_48[0xe] = '\0';
  local_48[0xf] = '\0';
  pcVar7 = local_48;
  do {
    pcVar6 = pcVar7 + 2;
    cVar1 = s_0123456789abcdef_ffff000090812714
            [(ulong)*(byte *)(param_2 + (ulong)(byte)(&DAT_ffff000090804988)[lVar4]) & 0xf];
    *pcVar7 = s_0123456789abcdef_ffff000090812714
              [*(byte *)(param_2 + (ulong)(byte)(&DAT_ffff000090804988)[lVar4]) >> 4];
    pcVar7[1] = cVar1;
    if (((uint)lVar4 < 10) && ((1 << (ulong)((uint)lVar4 & 0x1f) & 0x2a8U) != 0)) {
      pcVar6 = pcVar7 + 3;
      pcVar7[2] = '-';
    }
    lVar4 = lVar4 + 1;
    pcVar7 = pcVar6;
  } while (lVar4 != 0x10);
  *pcVar6 = '\0';
  uVar3 = FUN_ffff00009077f16c(local_48,0x25);
  if (0 < (int)uVar3) {
    uVar2 = 0;
    do {
      uVar5 = uVar2;
      if (uVar5 < 0x25) {
        param_1[uVar5] = local_48[uVar5];
      }
      uVar2 = uVar5 + 1;
    } while ((ulong)uVar3 != uVar5 + 1);
    param_1 = param_1 + uVar3;
    if (0x23 < uVar5) {
      return param_1;
    }
  }
  *param_1 = 0;
  return param_1;
}


