
void FUN_ffff00009078d0c8(long param_1,undefined8 *param_2)

{
  ulong uVar1;
  char cVar2;
  byte bVar3;
  undefined4 uVar4;
  ushort extraout_var;
  undefined8 uVar5;
  byte *pbVar6;
  ulong uVar7;
  byte *pbVar8;
  byte *pbVar9;
  
  pbVar9 = *(byte **)(param_1 + 0x20);
  cVar2 = *(char *)(param_1 + 0x41);
  FUN_ffff00009077ee50(pbVar9,0,0x10);
  uVar4 = FUN_ffff00009077b334(*param_2);
  uVar5 = *param_2;
  *(undefined4 *)(pbVar9 + 8) = uVar4;
  FUN_ffff00009077b334(uVar5);
  if (cVar2 == '\0') {
    *(short *)(pbVar9 + 2) = (short)*(undefined4 *)(param_2 + 1);
    uVar7 = *(ulong *)(param_1 + 0x18);
    uVar1 = *(ulong *)(param_1 + 0x20);
    *pbVar9 = *pbVar9 | 0x80;
    if (uVar1 < uVar7) {
      pbVar6 = (byte *)(uVar1 + 0x10);
      pbVar8 = *(byte **)(param_1 + 0x10);
    }
    else {
      pbVar6 = *(byte **)(param_1 + 0x10);
      pbVar8 = pbVar6;
    }
    *(byte **)(param_1 + 0x20) = pbVar6;
    if (((long)pbVar6 - (long)pbVar8 & 0xfffffffc0U) == 0) {
      uVar7 = *(long *)(param_1 + 8) + ((long)pbVar6 - (long)pbVar8 & 0xffffffff0U);
    }
    else {
      uVar7 = 0;
    }
    bVar3 = *pbVar6;
    *(int *)(pbVar9 + 4) = (int)uVar7;
    *pbVar6 = bVar3 & 0xfe;
    pbVar9[0xe] = (byte)(uVar7 >> 0x1c) & 0xf0 | (byte)extraout_var & 0xf;
    pbVar9[1] = 0;
    DataSynchronizationBarrier(3,3,0);
    pbVar9[1] = -(*pbVar9 + pbVar9[1] + pbVar9[2] + pbVar9[3] + pbVar9[4] + pbVar9[5] + pbVar9[6] +
                  pbVar9[7] + pbVar9[8] + pbVar9[9] + pbVar9[10] +
                 pbVar9[0xb] + pbVar9[0xc] + pbVar9[0xd] + pbVar9[0xe] + pbVar9[0xf]) - 2;
    DataSynchronizationBarrier(3,3,0);
  }
  else {
    *(short *)(pbVar9 + 0xc) = (short)*(undefined4 *)(param_2 + 1);
    uVar7 = *(ulong *)(param_1 + 0x18);
    uVar1 = *(ulong *)(param_1 + 0x20);
    *pbVar9 = *pbVar9 | 0x80;
    if (uVar1 < uVar7) {
      pbVar6 = (byte *)(uVar1 + 0x10);
      pbVar8 = *(byte **)(param_1 + 0x10);
    }
    else {
      pbVar6 = *(byte **)(param_1 + 0x10);
      pbVar8 = pbVar6;
    }
    *(byte **)(param_1 + 0x20) = pbVar6;
    if (((long)pbVar6 - (long)pbVar8 & 0xfffffffc0U) == 0) {
      uVar7 = *(long *)(param_1 + 8) + ((long)pbVar6 - (long)pbVar8 & 0xffffffff0U);
    }
    else {
      uVar7 = 0;
    }
    bVar3 = *pbVar6;
    *(int *)(pbVar9 + 4) = (int)uVar7;
    *pbVar6 = bVar3 & 0xfe;
    *(ushort *)(pbVar9 + 2) = (ushort)(uVar7 >> 0x1c) & 0xf0 | extraout_var & 0xf;
    pbVar9[1] = 0;
    DataSynchronizationBarrier(3,3,0);
    pbVar9[1] = -(*pbVar9 + pbVar9[1] + pbVar9[2] + pbVar9[3] + pbVar9[4] + pbVar9[5] + pbVar9[6] +
                  pbVar9[7] + pbVar9[8] + pbVar9[9] + pbVar9[10] +
                 pbVar9[0xb] + pbVar9[0xc] + pbVar9[0xd] + pbVar9[0xe] + pbVar9[0xf]) - 2;
    DataSynchronizationBarrier(3,3,0);
  }
  *pbVar9 = *pbVar9 | 1;
  FUN_ffff000090707c70(pbVar9,0x10);
  param_2[5] = pbVar9;
  return;
}


