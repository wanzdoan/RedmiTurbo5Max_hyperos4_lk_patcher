// ffff0000907852c8  size=140  FUN_ffff0000907852c8

void FUN_ffff0000907852c8(undefined8 param_1)

{
  undefined *puVar1;
  undefined8 *puVar2;
  
  puVar1 = PTR_PTR_ffff000090839a10;
  for (puVar2 = (undefined8 *)PTR_PTR_ffff000090839a18; puVar2 != (undefined8 *)puVar1;
      puVar2 = puVar2 + 2) {
    FUN_ffff00009077de54
              (s__s__d__runs__s_hook_function_ffff0000907e02f0,s_set_fdt_ffff0000907d5181,0x22,
               puVar2[1]);
    (*(code *)*puVar2)(param_1);
  }
  return;
}


