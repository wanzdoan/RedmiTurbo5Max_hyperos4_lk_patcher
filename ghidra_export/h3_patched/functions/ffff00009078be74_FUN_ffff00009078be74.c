// ffff00009078be74  size=508  FUN_ffff00009078be74

void FUN_ffff00009078be74(long param_1,int param_2)

{
  int iVar1;
  undefined1 uVar2;
  char cVar3;
  byte bVar4;
  undefined2 uVar5;
  undefined1 uVar6;
  undefined8 *puVar7;
  undefined8 uVar8;
  int iVar9;
  ulong uVar10;
  undefined2 *puVar11;
  long lVar12;
  
  iVar9 = 7;
  if (param_2 != 0) {
    iVar9 = 0xd;
  }
  iVar9 = (uint)*(byte *)(param_1 + 0x1b) * iVar9;
  if (0xffffff01 < iVar9 - 0xeeU) {
    puVar7 = (undefined8 *)FUN_ffff00009077d24c(iVar9 + 0x22);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff00009077de54(s__USB__alloc_desc_failed__type__d_ffff0000907e3abf,2);
      return;
    }
    iVar1 = iVar9 + 0x12;
    FUN_ffff00009077eb88(puVar7,0,iVar9 + 0x22);
    uVar8 = *(undefined8 *)(param_1 + 0x20);
    *(undefined2 *)(puVar7 + 1) = 0x200;
    *(undefined2 *)((long)puVar7 + 0xc) = 0x209;
    uVar6 = 0x80;
    if (param_2 != 0) {
      uVar6 = 0x32;
    }
    *(char *)((long)puVar7 + 0xf) = (char)((uint)iVar1 >> 8);
    uVar2 = *(undefined1 *)(param_1 + 0x1b);
    *(undefined1 *)((long)puVar7 + 0x14) = uVar6;
    uVar5 = *(undefined2 *)(param_1 + 0x18);
    *(undefined4 *)((long)puVar7 + 0x15) = 0x409;
    uVar6 = *(undefined1 *)(param_1 + 0x1a);
    *(short *)((long)puVar7 + 10) = (short)iVar1;
    *(char *)((long)puVar7 + 0xe) = (char)iVar1;
    *(undefined4 *)(puVar7 + 2) = 0x80000101;
    *(undefined1 *)((long)puVar7 + 0x19) = uVar2;
    *(undefined2 *)((long)puVar7 + 0x1a) = uVar5;
    *puVar7 = 0;
    *(undefined1 *)((long)puVar7 + 0x1c) = uVar6;
    uVar6 = FUN_ffff00009078c070(uVar8,param_2);
    cVar3 = *(char *)(param_1 + 0x1b);
    *(undefined1 *)((long)puVar7 + 0x1d) = uVar6;
    if (cVar3 != '\0') {
      uVar10 = 0;
      puVar11 = (undefined2 *)((long)puVar7 + 0x1e);
      uVar6 = 2;
      if (param_2 != 0) {
        uVar6 = 4;
      }
      do {
        lVar12 = *(long *)(*(long *)(param_1 + 0x30) + uVar10 * 8);
        *puVar11 = 0x507;
        cVar3 = *(char *)(lVar12 + 0x41);
        bVar4 = *(byte *)(lVar12 + 0x40);
        *(undefined2 *)((long)puVar11 + 3) = 2;
        *(undefined1 *)((long)puVar11 + 5) = uVar6;
        *(byte *)(puVar11 + 1) = bVar4 | (cVar3 != '\0') << 7;
        *(bool *)(puVar11 + 3) = *(char *)(lVar12 + 0x41) != '\0';
        if (param_2 == 1) {
          *(undefined4 *)((long)puVar11 + 7) = 0xf3006;
          *(undefined2 *)((long)puVar11 + 0xb) = 0;
          puVar11 = (undefined2 *)((long)puVar11 + 0xd);
        }
        else {
          puVar11 = (undefined2 *)((long)puVar11 + 7);
        }
        uVar10 = uVar10 + 1;
      } while (uVar10 < *(byte *)(param_1 + 0x1b));
    }
    lVar12 = 0x148;
    if (param_2 != 0) {
      lVar12 = 0x158;
    }
    uVar8 = *(undefined8 *)(&DAT_ffff00009088a020 + lVar12);
    *(undefined8 **)(&DAT_ffff00009088a020 + lVar12) = puVar7;
    *puVar7 = uVar8;
    return;
  }
  FUN_ffff00009077de54
            (s__USB___s_invalid_argument_ffff0000908016e8,s_udc_descriptor_alloc_ffff0000907d3949);
  return;
}


