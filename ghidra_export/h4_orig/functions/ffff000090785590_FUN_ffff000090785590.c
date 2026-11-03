// ffff000090785590  size=140  FUN_ffff000090785590

void FUN_ffff000090785590(undefined8 param_1)

{
  undefined *puVar1;
  undefined8 *puVar2;
  
  puVar1 = PTR_PTR_ffff000090839a30;
  for (puVar2 = (undefined8 *)PTR_PTR_ffff000090839a38; puVar2 != (undefined8 *)puVar1;
      puVar2 = puVar2 + 2) {
    FUN_ffff00009077e11c
              (s__s__d__runs__s_hook_function_ffff0000907e03aa,s_set_fdt_ffff0000907d51be,0x22,
               puVar2[1]);
    (*(code *)*puVar2)(param_1);
  }
  return;
}


