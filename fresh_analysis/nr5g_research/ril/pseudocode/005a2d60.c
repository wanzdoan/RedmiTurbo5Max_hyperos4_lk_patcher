
undefined8 xiaomi_build_region_list_contain(undefined8 param_1,ulong param_2)

{
  char cVar1;
  undefined8 uVar2;
  
  cVar1 = xiaomi_string_list_contain(param_1,param_2,"all_region");
  if (cVar1 != '\0') {
    return 1;
  }
  xiaomi_build_region_get();
  uVar2 = xiaomi_string_list_contain(param_1,param_2 & 0xffffffff,&DAT_005ef45c);
  return uVar2;
}


