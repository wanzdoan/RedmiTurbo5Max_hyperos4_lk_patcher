
undefined8 * FUN_ffff0000907703fc(long param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  long *plVar4;
  undefined8 *puVar5;
  undefined8 uVar6;
  undefined4 local_28;
  undefined4 local_24;
  
  iVar1 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907ea7ef);
  if (iVar1 < 0) {
    plVar4 = (long *)thunk_FUN_ffff00009077d514(0x10);
    if (plVar4 == (long *)0x0) {
      return (undefined8 *)0x0;
    }
    *plVar4 = 0;
    puVar5 = (undefined8 *)thunk_FUN_ffff00009077d514(0x28);
    if (puVar5 != (undefined8 *)0x0) {
      puVar5[2] = 0;
      puVar5[4] = 0;
      *puVar5 = plVar4;
LAB_ffff000090770560:
      puVar5[1] = 2;
      *(undefined4 *)(puVar5 + 3) = 0;
      return puVar5;
    }
  }
  else {
    local_28 = 0;
    iVar2 = FUN_ffff00009076cd48(param_1,iVar1,&local_28);
    plVar4 = (long *)thunk_FUN_ffff00009077d514(0x10);
    if (iVar2 == 1) {
      if (plVar4 == (long *)0x0) {
        return (undefined8 *)0x0;
      }
      *plVar4 = param_1;
      puVar5 = (undefined8 *)thunk_FUN_ffff00009077d514(0x28);
      if (puVar5 != (undefined8 *)0x0) {
        *puVar5 = plVar4;
        puVar5[2] = 0;
        puVar5[4] = 0;
        *(undefined4 *)(puVar5 + 1) = 2;
        *(uint *)((long)puVar5 + 0xc) = (uint)(param_1 != 0);
        *(undefined4 *)(puVar5 + 3) = 0;
        uVar6 = FUN_ffff0000907705a0(param_1,iVar1,&local_28,1,param_3);
        puVar5[2] = uVar6;
        uVar3 = FUN_ffff0000907702c0();
        uVar6 = thunk_FUN_ffff00009077d514
                          (-(ulong)(uVar3 >> 0x1f) & 0xfffffff000000000 | (ulong)uVar3 << 4);
        local_24 = 0;
        FUN_ffff000090770350(puVar5[2],uVar6,&local_24);
        thunk_FUN_ffff00009077e244(uVar6,(long)(int)uVar3,0x10,&LAB_ffff0000907702a0);
        puVar5[3] = (ulong)uVar3;
        puVar5[4] = uVar6;
        return puVar5;
      }
    }
    else {
      if (plVar4 == (long *)0x0) {
        return (undefined8 *)0x0;
      }
      *plVar4 = 0;
      puVar5 = (undefined8 *)thunk_FUN_ffff00009077d514(0x28);
      if (puVar5 != (undefined8 *)0x0) {
        puVar5[2] = 0;
        puVar5[4] = 0;
        *puVar5 = plVar4;
        goto LAB_ffff000090770560;
      }
    }
  }
  thunk_FUN_ffff0000907c314c(plVar4);
  return (undefined8 *)0x0;
}


