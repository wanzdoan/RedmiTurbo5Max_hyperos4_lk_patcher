// ffff000090734e14  size=160  FUN_ffff000090734e14

undefined8 FUN_ffff000090734e14(long param_1,undefined4 *param_2)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  undefined8 uVar4;
  long lVar5;
  
  if ((param_1 == 0) || (param_2 == (undefined4 *)0x0)) {
    FUN_ffff00009077de54(s__PART_UTILS__Need_buffer_with__d_ffff0000907df915,0xc0);
    uVar4 = 0xffffffea;
  }
  else {
    iVar1 = FUN_ffff0000907356e8();
    if (iVar1 < 0) {
      uVar4 = 0xfffffffb;
    }
    else {
      lVar3 = FUN_ffff0000907355f8();
      iVar1 = FUN_ffff00009073560c();
      if (0 < iVar1) {
        lVar5 = 0;
        do {
          *(long *)(param_1 + lVar5 * 8) = lVar3;
          lVar5 = lVar5 + 1;
          iVar1 = FUN_ffff00009073560c();
          lVar3 = lVar3 + 0x6d;
        } while (lVar5 < iVar1);
      }
      uVar2 = FUN_ffff00009073560c();
      uVar4 = 0;
      *param_2 = uVar2;
    }
  }
  return uVar4;
}


