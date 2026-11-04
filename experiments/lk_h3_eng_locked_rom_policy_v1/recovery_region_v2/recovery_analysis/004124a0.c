
/* android::base::EqualsIgnoreCase(std::basic_string_view<char, std::char_traits<char> >,
   std::basic_string_view<char, std::char_traits<char> >) */

bool android::base::EqualsIgnoreCase(char *param_1,size_t param_2,char *param_3,size_t param_4)

{
  int iVar1;

  if (param_2 == param_4) {
    iVar1 = strncasecmp(param_1,param_3,param_2);
    return iVar1 == 0;
  }
  return false;
}
