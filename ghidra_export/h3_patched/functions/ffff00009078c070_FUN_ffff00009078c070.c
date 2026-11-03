// ffff00009078c070  size=332  FUN_ffff00009078c070

uint FUN_ffff00009078c070(undefined1 *param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  int iVar3;
  undefined1 uVar4;
  int iVar5;
  undefined8 *puVar6;
  uint uVar7;
  undefined1 *puVar8;
  undefined8 uVar9;
  
  lVar2 = 0x150;
  if (param_2 != 0) {
    lVar2 = 0x160;
  }
  if ((param_1 != (undefined1 *)0x0) &&
     (uVar7 = *(uint *)(&DAT_ffff00009088a020 + lVar2), uVar7 < 0x100)) {
    iVar5 = FUN_ffff00009077ef88(param_1);
    if ((uint)(iVar5 * 2) < 0xfe) {
      uVar1 = iVar5 * 2 + 2;
      puVar6 = (undefined8 *)FUN_ffff00009077d24c((ulong)uVar1 + 0x10);
      if (puVar6 != (undefined8 *)0x0) {
        FUN_ffff00009077eb88(puVar6,0,(ulong)uVar1 + 0x10);
        iVar3 = *(int *)(&DAT_ffff00009088a020 + lVar2);
        uVar7 = uVar7 | 0x300;
        *(short *)((long)puVar6 + 10) = (short)uVar1;
        *(char *)((long)puVar6 + 0xc) = (char)uVar1;
        *(short *)(puVar6 + 1) = (short)uVar7;
        *(undefined1 *)((long)puVar6 + 0xd) = 3;
        *(int *)(&DAT_ffff00009088a020 + lVar2) = iVar3 + 1;
        if (iVar5 != 0) {
          puVar8 = (undefined1 *)((long)puVar6 + 0xe);
          do {
            uVar4 = *param_1;
            puVar8[1] = 0;
            iVar5 = iVar5 + -1;
            *puVar8 = uVar4;
            puVar8 = puVar8 + 2;
            param_1 = param_1 + 1;
          } while (iVar5 != 0);
          uVar7 = (uint)*(byte *)(puVar6 + 1);
        }
        lVar2 = 0x148;
        if (param_2 != 0) {
          lVar2 = 0x158;
        }
        uVar9 = *(undefined8 *)(&DAT_ffff00009088a020 + lVar2);
        *(undefined8 **)(&DAT_ffff00009088a020 + lVar2) = puVar6;
        *puVar6 = uVar9;
        return uVar7 & 0xff;
      }
      FUN_ffff00009077de54(s__USB__alloc_desc_failed__type__d_ffff0000907e3abf,3);
    }
    else {
      FUN_ffff00009077de54
                (s__USB___s_invalid_argument_ffff0000908016e8,
                 s_udc_descriptor_alloc_ffff0000907d3949);
    }
  }
  return 0;
}


