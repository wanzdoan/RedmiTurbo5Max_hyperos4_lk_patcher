
undefined8 FUN_ffff0000be066190(uint param_1,int param_2)

{
  ulong uVar1;
  char cVar2;
  ulong uVar3;
  long lVar4;
  undefined *puVar5;
  
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  uVar1 = (ulong)param_1;
  cVar2 = (&DAT_ffff0000be0f0580)[uVar1];
  uVar3 = (ulong)cVar2;
  if (((uVar3 == 0xffffffffffffffff) && ((&DAT_ffff0000be0f0f08)[uVar1] == -1)) &&
     ((&DAT_ffff0000be0f1890)[uVar1] == -1)) {
    return 0xffffffff;
  }
  if (param_2 == 0) {
    if (cVar2 == -1) {
      lVar4 = 4;
LAB_ffff0000be0662d4:
      cVar2 = (&DAT_ffff0000be0f0f08)[uVar1];
      if (cVar2 == -1) {
code_r0xffff0000be066380:
        puVar5 = &DAT_ffff0000be0f1018;
        cVar2 = (&DAT_ffff0000be0f1890)[uVar1];
        goto LAB_ffff0000be0662e8;
      }
    }
    else {
      *(int *)(*(long *)(&DAT_ffff0000be0efd08 + uVar1 * 8) + 8) = (int)(1L << (uVar3 & 0x3f));
      cVar2 = (&DAT_ffff0000be0f0f08)[uVar1];
      if (cVar2 == -1) {
        return 0;
      }
      lVar4 = 4;
    }
  }
  else {
    if (param_2 == 1) {
      if (cVar2 == -1) {
        lVar4 = 8;
        goto LAB_ffff0000be0662d4;
      }
      *(int *)(*(long *)(&DAT_ffff0000be0efd08 + uVar1 * 8) + 4) = (int)(1L << (uVar3 & 0x3f));
code_r0xffff0000be066280:
      cVar2 = (&DAT_ffff0000be0f0f08)[uVar1];
      if (cVar2 == -1) {
        return 0;
      }
    }
    else {
      if (param_2 != 2) {
        return 0xfffffffe;
      }
      if (cVar2 != -1) {
        *(int *)(*(long *)(&DAT_ffff0000be0efd08 + uVar1 * 8) + 8) = (int)(1L << (uVar3 & 0x3f));
        goto code_r0xffff0000be066280;
      }
      cVar2 = (&DAT_ffff0000be0f0f08)[uVar1];
      if (cVar2 == -1) {
        if ((&DAT_ffff0000be0f1890)[uVar1] == -1) {
          lVar4 = 4;
        }
        else {
          *(int *)(*(long *)(&DAT_ffff0000be0f19a0 + uVar1 * 8) + 8) =
               (int)(1L << ((long)(char)(&DAT_ffff0000be0f2218)[uVar1] & 0x3fU));
          lVar4 = 4;
          *(int *)(*(long *)(&DAT_ffff0000be0f2328 + uVar1 * 8) + 8) =
               (int)(1L << ((long)(char)(&DAT_ffff0000be0f2ba0)[uVar1] & 0x3fU));
        }
        goto code_r0xffff0000be066380;
      }
    }
    lVar4 = 8;
  }
  puVar5 = &DAT_ffff0000be0f0690;
LAB_ffff0000be0662e8:
  *(int *)(*(long *)(puVar5 + uVar1 * 8) + lVar4) = (int)(1L << ((long)cVar2 & 0x3fU));
  return 0;
}


