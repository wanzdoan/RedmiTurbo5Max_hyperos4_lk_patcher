// ffff00009077be30  size=208  FUN_ffff00009077be30

undefined8 FUN_ffff00009077be30(long param_1,ulong param_2)

{
  long *plVar1;
  ulong uVar2;
  ulong uVar3;
  long *plVar4;
  
  FUN_ffff000090717738(&DAT_ffff0000908328a8,0xffffffff);
  if (param_1 != 0) {
    for (plVar4 = *(long **)(param_1 + 0x50); plVar4 != (long *)(param_1 + 0x48);
        plVar4 = (long *)plVar4[1]) {
      uVar2 = plVar4[7];
      if ((uVar2 <= param_2) && (uVar3 = plVar4[8], param_2 <= (uVar2 + uVar3) - 1)) {
        plVar1 = (long *)plVar4[1];
        *plVar1 = *plVar4;
        *(long **)(*plVar4 + 8) = plVar1;
        *plVar4 = 0;
        plVar4[1] = 0;
        FUN_ffff0000907091a8(param_1 + 0x58,uVar2,uVar3 >> 0xc);
        FUN_ffff0000907177d0(&DAT_ffff0000908328a8);
        FUN_ffff00009077aecc(plVar4 + 9);
        thunk_FUN_ffff0000907c314c(plVar4);
        return 0;
      }
    }
  }
  FUN_ffff0000907177d0(&DAT_ffff0000908328a8);
  return 0xfffffffe;
}


