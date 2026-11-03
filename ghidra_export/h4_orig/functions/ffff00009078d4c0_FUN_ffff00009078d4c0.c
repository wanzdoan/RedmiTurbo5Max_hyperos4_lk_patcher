// ffff00009078d4c0  size=172  FUN_ffff00009078d4c0

void FUN_ffff00009078d4c0(long param_1)

{
  uint *puVar1;
  long lVar2;
  byte bVar3;
  int iVar4;
  char *pcVar5;
  byte *pbVar6;
  
  lVar2 = -0xffffe98fea00;
  if (*(char *)(param_1 + 0x41) != '\0') {
    lVar2 = -0xffffe98feb00;
  }
  puVar1 = (uint *)(lVar2 + (ulong)*(byte *)(param_1 + 0x40) * 0x10);
  if ((*puVar1 >> 0xf & 1) == 0) {
    pcVar5 = s__USB__QMU___s_s_qmu_is_inactive_n_ffff0000907f204f;
  }
  else {
    *puVar1 = 4;
    iVar4 = FUN_ffff00009078b294(puVar1,0x8000,0,10,100);
    if (iVar4 == 0) goto LAB_ffff00009078d53c;
    pcVar5 = s__USB__QMU__stop__s_s_qmu_failed_ffff0000907d6e08;
  }
  FUN_ffff00009077e11c(pcVar5,param_1 + 0x30);
LAB_ffff00009078d53c:
  pbVar6 = *(byte **)(param_1 + 0x10);
  if (pbVar6 != (byte *)0x0) {
    bVar3 = *pbVar6;
    *(byte **)(param_1 + 0x28) = pbVar6;
    *pbVar6 = bVar3 & 0xfe;
    *(byte **)(param_1 + 0x18) = pbVar6 + 0x30;
    *(byte **)(param_1 + 0x20) = pbVar6;
  }
  return;
}


