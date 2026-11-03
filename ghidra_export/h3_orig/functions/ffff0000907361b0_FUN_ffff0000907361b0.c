// ffff0000907361b0  size=68  FUN_ffff0000907361b0

undefined2 FUN_ffff0000907361b0(undefined8 *param_1)

{
  long lVar1;
  undefined2 local_14 [2];
  
  lVar1 = param_1[2];
  local_14[0] = 0;
  (*(code *)*param_1)(*(undefined2 *)(lVar1 + 0x1e),local_14,*(undefined2 *)(lVar1 + 0x20),
                      *(undefined2 *)(lVar1 + 0x22));
  return local_14[0];
}


