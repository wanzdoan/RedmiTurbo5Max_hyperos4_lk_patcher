
/* get_dev_rom_zone(char*) */

int get_dev_rom_zone(char *param_1)

{
  int iVar1;

  puts("Get device rom zone.");
  property_get("ro.rom.zone",param_1,"unknown");
  iVar1 = strcmp(param_1,"unknown");
  return -(uint)(iVar1 == 0);
}
