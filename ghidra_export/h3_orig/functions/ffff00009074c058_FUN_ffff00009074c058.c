// ffff00009074c058  size=120  FUN_ffff00009074c058

undefined8 FUN_ffff00009074c058(int param_1,undefined8 param_2,uint param_3)

{
  uint uVar1;
  long *plVar2;
  long lVar3;
  
  uVar1 = 0;
  if (param_1 != 0x2b) {
    uVar1 = (uint)(param_1 != 0x2d);
  }
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  lVar3 = lVar3 - (ulong)uVar1;
  plVar2 = (long *)(s_0123456789abcdef_ffff000090804a48 + (ulong)uVar1 * 8 + 0x10);
  do {
    lVar3 = lVar3 + -1;
    *(uint *)(*plVar2 + 0x34) = *(uint *)(*plVar2 + 0x34) & 0xfffffffc | param_3 & 3;
    plVar2 = plVar2 + 1;
  } while (lVar3 != 0);
  FUN_ffff00009077de54
            (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db0ef,s_DSI_SetMode_ffff0000907e9713);
  return 0;
}


