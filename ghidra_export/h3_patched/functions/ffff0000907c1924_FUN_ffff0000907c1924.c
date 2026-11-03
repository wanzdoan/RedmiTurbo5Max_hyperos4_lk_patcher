// ffff0000907c1924  size=156  FUN_ffff0000907c1924

void FUN_ffff0000907c1924(long param_1,char *param_2)

{
  char *pcVar1;
  ulong uVar2;
  char cVar3;
  long lVar4;
  ulong uVar5;
  char *pcVar6;
  char local_50 [32];
  long local_18;
  
  local_50[0x10] = '\0';
  local_50[0x11] = '\0';
  local_50[0x12] = '\0';
  local_50[0x13] = '\0';
  local_50[0x14] = '\0';
  local_50[0x15] = '\0';
  local_50[0x16] = '\0';
  local_50[0x17] = '\0';
  local_50[0x18] = '\0';
  local_50[0x19] = '\0';
  local_50[0x1a] = '\0';
  local_50[0x1b] = '\0';
  local_50[0x1c] = '\0';
  local_50[0x1d] = '\0';
  local_50[0x1e] = '\0';
  local_50[0x1f] = '\0';
  local_50[0] = '\0';
  local_50[1] = '\0';
  local_50[2] = '\0';
  local_50[3] = '\0';
  local_50[4] = '\0';
  local_50[5] = '\0';
  local_50[6] = '\0';
  local_50[7] = '\0';
  local_50[8] = '\0';
  local_50[9] = '\0';
  local_50[10] = '\0';
  local_50[0xb] = '\0';
  local_50[0xc] = '\0';
  local_50[0xd] = '\0';
  local_50[0xe] = '\0';
  local_50[0xf] = '\0';
  uVar2 = 0;
  local_18 = param_1;
  do {
    uVar5 = uVar2;
    cVar3 = FUN_ffff0000907c09ac(&local_18);
    lVar4 = local_18;
    uVar2 = uVar5 + 1;
    local_50[uVar5] = cVar3 + '0';
    if (lVar4 == 0) break;
  } while (uVar5 < 0x1e);
  lVar4 = uVar5 - 1;
  pcVar6 = param_2;
  do {
    pcVar1 = local_50 + uVar5;
    lVar4 = lVar4 + -1;
    uVar5 = uVar5 - 1;
    *pcVar6 = *pcVar1;
    pcVar6 = pcVar6 + 1;
  } while (lVar4 != -2);
  param_2[uVar2] = '\0';
  return;
}


