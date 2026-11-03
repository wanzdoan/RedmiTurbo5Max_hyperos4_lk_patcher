// ffff000090736060  size=56  FUN_ffff000090736060

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090736060(code *param_1,long param_2)

{
  long in_x9;
  
  _DAT_ffff00009086db38 = param_2;
  _DAT_ffff00009086db40 = in_x9;
  (*param_1)(*(undefined2 *)(in_x9 + 8),param_2 + 0x30);
  return 0;
}


