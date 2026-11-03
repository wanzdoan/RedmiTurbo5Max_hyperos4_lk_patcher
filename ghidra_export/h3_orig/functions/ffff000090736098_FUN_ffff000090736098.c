// ffff000090736098  size=204  FUN_ffff000090736098

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090736098(int param_1)

{
  ulong uVar1;
  undefined2 *puVar2;
  long lVar3;
  short local_1c [2];
  short local_18 [2];
  short local_14 [2];
  
  if (_DAT_ffff00009086db48 == (undefined8 *)0x0) {
    uVar1 = 0xffffffda;
  }
  else {
    if (param_1 == 2) {
      lVar3 = _DAT_ffff00009086db48[2];
      local_14[0] = 0;
      (*(code *)*_DAT_ffff00009086db48)
                (*(undefined2 *)(lVar3 + 0xc),local_14,*(undefined2 *)(lVar3 + 0xe),
                 *(undefined2 *)(lVar3 + 0x10));
      local_1c[0] = local_14[0];
    }
    else if (param_1 == 1) {
      lVar3 = _DAT_ffff00009086db48[2];
      local_18[0] = 0;
      (*(code *)*_DAT_ffff00009086db48)
                (*(undefined2 *)(lVar3 + 6),local_18,*(undefined2 *)(lVar3 + 8),
                 *(undefined2 *)(lVar3 + 10));
      local_1c[0] = local_18[0];
    }
    else {
      if (param_1 != 0) {
        return 0xfffffff8;
      }
      puVar2 = (undefined2 *)_DAT_ffff00009086db48[2];
      local_1c[0] = 0;
      (*(code *)*_DAT_ffff00009086db48)(*puVar2,local_1c,puVar2[1],puVar2[2]);
    }
    uVar1 = (ulong)(local_1c[0] == 0);
  }
  return uVar1;
}


