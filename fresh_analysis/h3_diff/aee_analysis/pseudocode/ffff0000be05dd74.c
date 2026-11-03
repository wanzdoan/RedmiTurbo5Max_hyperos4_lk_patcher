
undefined8 FUN_ffff0000be05dd74(long param_1,ulong param_2)

{
  long *plVar1;
  ulong uVar2;
  ulong uVar3;
  long *plVar4;
  
  FUN_ffff0000be01a730(&DAT_ffff0000be0ef2a0,0xffffffff);
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
        FUN_ffff0000be008854(param_1 + 0x58,uVar2,uVar3 >> 0xc);
        FUN_ffff0000be01a7c8(&DAT_ffff0000be0ef2a0);
        FUN_ffff0000be05cdc0(plVar4 + 9);
        thunk_FUN_ffff0000be09937c(plVar4);
        return 0;
      }
    }
  }
  FUN_ffff0000be01a7c8(&DAT_ffff0000be0ef2a0);
  return 0xfffffffe;
}


