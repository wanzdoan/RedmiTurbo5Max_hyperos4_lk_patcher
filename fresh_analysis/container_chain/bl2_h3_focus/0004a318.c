// File offset entry: 0x4a318

undefined8 FUN_ffff0000b804a318(long param_1,ulong param_2)

{
  long *plVar1;
  ulong uVar2;
  ulong uVar3;
  long *plVar4;
  
  FUN_ffff0000b800ad90(&DAT_ffff0000b8100fd0,0xffffffff);
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
        FUN_ffff0000b8008ae4(param_1 + 0x58,uVar2,uVar3 >> 0xc);
        FUN_ffff0000b800ae28(&DAT_ffff0000b8100fd0);
        FUN_ffff0000b80495d4(plVar4 + 9);
        thunk_FUN_ffff0000b8081d1c(plVar4);
        return 0;
      }
    }
  }
  FUN_ffff0000b800ae28(&DAT_ffff0000b8100fd0);
  return 0xfffffffe;
}


