
void FUN_ffff00009078bff4(long param_1,int param_2)

{
  long lVar1;
  bool bVar2;
  undefined1 uVar3;
  undefined8 *puVar4;
  undefined8 uVar5;
  
  puVar4 = (undefined8 *)FUN_ffff00009077d514(0x22);
  if (puVar4 != (undefined8 *)0x0) {
    FUN_ffff00009077ee50(puVar4,0,0x22);
    bVar2 = param_2 != 0;
    *(undefined4 *)(puVar4 + 1) = 0x120100;
    uVar3 = 2;
    if (bVar2) {
      uVar3 = 3;
    }
    *(undefined2 *)((long)puVar4 + 0xc) = 0x112;
    *puVar4 = 0;
    *(undefined1 *)((long)puVar4 + 0xf) = uVar3;
    uVar3 = 0x40;
    if (bVar2) {
      uVar3 = 9;
    }
    *(undefined1 *)((long)puVar4 + 0xe) = 0;
    *(undefined2 *)(puVar4 + 2) = 0;
    *(undefined1 *)((long)puVar4 + 0x13) = uVar3;
    *(undefined1 *)((long)puVar4 + 0x12) = 0;
    lVar1 = 0x148;
    if (bVar2) {
      lVar1 = 0x158;
    }
    FUN_ffff00009077eb20((long)puVar4 + 0x14,param_1,2);
    FUN_ffff00009077eb20((long)puVar4 + 0x16,param_1 + 2,2);
    FUN_ffff00009077eb20(puVar4 + 3,param_1 + 4,2);
    uVar3 = FUN_ffff00009078c330(*(undefined8 *)(param_1 + 8),param_2);
    uVar5 = *(undefined8 *)(param_1 + 0x10);
    *(undefined1 *)((long)puVar4 + 0x1a) = uVar3;
    uVar3 = FUN_ffff00009078c330(uVar5,param_2);
    uVar5 = *(undefined8 *)(param_1 + 0x18);
    *(undefined1 *)((long)puVar4 + 0x1b) = uVar3;
    uVar3 = FUN_ffff00009078c330(uVar5,param_2);
    *(undefined1 *)((long)puVar4 + 0x1c) = uVar3;
    uVar5 = *(undefined8 *)((long)&DAT_ffff00009088a0b8 + lVar1);
    *(undefined8 **)((long)&DAT_ffff00009088a0b8 + lVar1) = puVar4;
    *(undefined1 *)((long)puVar4 + 0x1d) = 1;
    *puVar4 = uVar5;
    return;
  }
  FUN_ffff00009077e11c(s__USB__alloc_desc_failed__type__d_ffff0000907e3ba7,1);
  return;
}


